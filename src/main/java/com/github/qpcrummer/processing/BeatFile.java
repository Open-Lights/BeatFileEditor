package com.github.qpcrummer.processing;

import com.github.qpcrummer.Main;
import com.github.qpcrummer.directory.Directory;
import com.github.qpcrummer.gui.TimeLine;
import com.github.qpcrummer.gui.Track;
import com.github.qpcrummer.music.MusicPlayer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class BeatFile {
    public static Path currentBeatFile;
    private static final String txt = ".txt";

    public static void saveAll() {
        for (Track track : TimeLine.tracks) {
            save(track.beats, channelsFromTracks(track));
        }
    }

    private static void save(List<?> beats, int[] channels) {
        if (MusicPlayer.currentSong != null) {
            String name = getSongName() + "-" + joinIntArray(channels) + txt;
            try {
                writeListToFile(beats, name);
            } catch (IOException e) {
                Main.logger.warning("Failed to save beat file");
                throw new RuntimeException(e);
            }
        } else {
            Main.logger.warning("Failed to save beat file because no song is loaded");
        }
    }

    private static void writeListToFile(List<?> list, String filePath) throws IOException {
        Path outputPath = Paths.get(Directory.savePath + "/" + filePath);

        List<String> lines = list.stream()
                .map(BeatFile::elementToString)
                .collect(Collectors.toList());

        Main.logger.info("Saving file");
        Files.writeString(outputPath, String.join(System.lineSeparator(), lines));
    }

    private static String elementToString(Object element) {
        if (element instanceof long[]) {
            return arrayToString((long[]) element);
        } else if (element instanceof Long) {
            return element.toString();
        }
        return "";
    }

    private static String arrayToString(long[] array) {
        return "[" + String.join(", ", LongStream.of(array).mapToObj(String::valueOf).toArray(String[]::new)) + "]";
    }

    private static String joinIntArray(int[] array) {
        return Arrays.stream(array)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("_"));
    }

    private static String getSongName() {
        return MusicPlayer.currentSong.getFileName().toString().replace(".wav", "");
    }

    private static int[] channelsFromTracks(Track track) {
        // Count the number of commas to determine the size of the array
        String input = track.getChannels();
        int numberOfCommas = input.length() - input.replace(",", "").length();
        int arraySize = numberOfCommas + 1;

        int[] numbers = new int[arraySize];

        // Define a pattern to match integers
        Pattern pattern = Pattern.compile("\\d+");

        // Create a matcher with the input string
        Matcher matcher = pattern.matcher(input);

        int index = 0;
        // Find all matches and add them to the array
        while (matcher.find()) {
            String match = matcher.group();
            int number = Integer.parseInt(match);
            numbers[index++] = number;
        }

        return numbers;
    }
}
