package io.github.jason13official.telecir.impl.server.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.world.level.saveddata.SavedData;

public abstract class UniqueNameGenerator extends SavedData {

  private final Random random;
  private final Set<String> usedNames = new HashSet<>();
  private final Map<String, String[]> vocab = new HashMap<>();

  public UniqueNameGenerator(long seed) {
    this.random = new Random(seed);
    initializeVocabulary();
  }

  private void initializeVocabulary() {

    // Name structure options
    vocab.put("options", new String[]{
        "<firstconsonant><ending>",
        "<firstconsonant><ending>",
        "<firstconsonant><ending>",
        "<firstconsonant><ending>",
        "<firstvowel><midletters><ending>",
        "<firstvowel><midletters><ending>",
        "<firstvowel><midletters><ending>",
        "<firstconsonant><vowel><midletters><ending>",
        "<firstconsonant><vowel><midletters><ending>",
        "<firstvowel><midletters><vowel><midletters><ending>"
    });

    // First vowels (capitalized)
    vocab.put("firstvowel", new String[]{
        "A", "Ae", "Ai", "Au", "E", "Ei", "I", "Ia", "O", "U"
    });

    // First consonants (capitalized)
    vocab.put("firstconsonant", new String[]{
        "B", "B", "B", "Bj", "Bl", "Br",
        "D", "D", "D", "Dr", "Dv",
        "F", "F", "F", "Fj", "Fl", "Fr",
        "G", "G", "G", "Gl", "Gn", "Gr", "Gy",
        "H", "H", "H", "Hj", "Hl", "Hn", "Hr", "Hv",
        "J", "J", "J",
        "K", "K", "K", "Kl", "Kn", "Kr", "Kv",
        "L", "L", "L", "Lj",
        "M", "Mj",
        "N", "N", "N", "Nj",
        "P", "P", "Pl", "Pr",
        "R", "R", "R",
        "S", "S", "S", "Sk", "Skj", "Skr", "Sl", "Sm", "Sn", "Sp", "St", "Sv",
        "T", "T", "T", "Th", "Th", "Th", "Thr", "Tr", "Tv",
        "V", "V", "V", "Vr"
    });

    // Middle vowels (lowercase)
    vocab.put("vowel", new String[]{
        "a", "ae", "au", "e", "ei", "i", "ia", "io", "o", "ou", "u", "uo", "y"
    });

    // Middle consonant combinations
    vocab.put("midletters", new String[]{
        "b", "b", "b", "bb",
        "d", "d", "d", "dd", "dr",
        "f", "f", "f", "fj", "fl", "fn", "fnh", "fr", "fthr",
        "g", "g", "g", "gg", "ggv", "gj", "gt", "gv",
        "j", "j", "j",
        "k", "k", "k", "kk",
        "l", "l", "l", "ld", "ldj", "lf", "lg", "lj", "lk", "ll", "lm", "lln", "ls", "lsv", "lv",
        "m", "m", "m", "mb", "mbl", "md", "mh", "ml", "mm", "mn", "ms",
        "n", "n", "n", "nd", "ndr", "ngm", "nh", "nnh", "nj", "nn", "nr", "nsk", "nt",
        "pn",
        "r", "r", "r", "rb", "rf", "rg", "rj", "rp", "rr", "rth", "rv",
        "stl",
        "t", "t", "t", "th", "thb", "thg", "thgr", "thj", "thm", "thn", "tl", "tn", "tr", "tt",
        "x"
    });

    // Word endings
    vocab.put("ending", new String[]{
        "a", "af", "al", "ald", "alf", "all", "am", "an", "ar", "arl", "arr",
        "eid", "eif", "eik", "einn", "eist", "en",
        "i", "ia", "iall", "iarn", "if", "igg", "ik", "ild", "il", "ill", "in", "ind", "ing", "inn",
        "iof", "ir", "ith",
        "oa", "od", "of", "ofn", "oi", "oin", "okk", "olf", "opt", "or", "ort", "ott",
        "ul", "un", "unn", "ur", "urr",
        "ym", "yn"
    });
  }

  private String getRandomElement(String[] array) {
    if (array == null || array.length == 0) {
      return "";
    }
    return array[random.nextInt(array.length)];
  }

  private String processTemplate(String line) {
    if (line == null || !line.contains("<")) {
      return line;
    }

    int tagStart = line.indexOf("<");
    int tagEnd = line.indexOf(">");

    if (tagStart == -1 || tagEnd == -1 || tagEnd <= tagStart) {
      return line;
    }

    String key = line.substring(tagStart + 1, tagEnd);
    String[] array = vocab.get(key);

    if (array != null) {
      String replacement = getRandomElement(array);
      line = line.substring(0, tagStart) + replacement + line.substring(tagEnd + 1);
    } else {
      // If key not found, remove the tag
      line = line.substring(0, tagStart) + line.substring(tagEnd + 1);
    }

    // Recursively process remaining tags
    return line.contains("<") ? processTemplate(line) : line;
  }

  private String generateName() {
    String[] optionsArray = vocab.get("options");
    String template = getRandomElement(optionsArray);
    return processTemplate(template).trim();
  }

  public final String generateUniqueName() {
    for (int attempts = 0; attempts < 1000; attempts++) {
      String name = generateName();
      if (!usedNames.contains(name)) {
        usedNames.add(name);
        return name;
      }
    }

    // Fallback for the astronomically unlikely case of too many collisions
    String fallbackName = generateName() + "_" + System.currentTimeMillis();
    usedNames.add(fallbackName);
    return fallbackName;
  }


  public final boolean releaseName(String name) {
    return usedNames.remove(name);
  }

  public final boolean blacklistName(String name) {
    return usedNames.add(name);
  }

}
