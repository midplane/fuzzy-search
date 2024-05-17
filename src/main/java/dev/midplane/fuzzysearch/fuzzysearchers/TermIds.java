package dev.midplane.fuzzysearch.fuzzysearchers;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TermIds {
    private List<Integer> ids;
    private List<Integer> frequencies;

    public TermIds() {
        ids = new ArrayList<>();
        frequencies = new ArrayList<>();
    }

    public void addId(int id, int frequency) {
        ids.add(id);
        frequencies.add(frequency);
    }

    public int getId(int index) {
        return ids.get(index);
    }

    public int getFrequency(int index) {
        return frequencies.get(index);
    }

    public int getLength() {
        return ids.size();
    }

}