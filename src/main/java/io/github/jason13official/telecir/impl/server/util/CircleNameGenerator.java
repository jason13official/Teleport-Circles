package io.github.jason13official.telecir.impl.server.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * A name generator that creates fantasy names using a template-based system with recursive
 * substitution of tagged placeholders. <br /> <a
 * href="https://springhole.net/writing_roleplaying_randomators/asgardyish-names.htm">Reference</a>
 */
public class CircleNameGenerator {

  private static final int MAX_UNIQUE_ATTEMPTS = 1000;
  private static final int MAX_RECURSION_ATTEMPTS = 10;

  private final Set<String> usedNames = new HashSet<>();
  private final Map<String, String[]> vocab = new HashMap<>();
  private final Random random;

//  public NameGenerator() {
//    this.random = new Random();
//    initializeVocabulary();
//  }

  public CircleNameGenerator(long seed) {
    this.random = new Random(seed);
    initializeVocabulary();
  }
  private void initializeVocabulary() {
    // Main template - generates 5 name options
    vocab.put("FIRST", new String[]{
        "<options>\n<options>\n<options>\n<options>\n<options>"
    });

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

  /**
   * Gets a random element from the specified array.
   *
   * @param array the array to select from
   * @return a random element from the array, or empty string if array is null/empty
   */
  private String getRandomElement(String[] array) {
    if (array == null || array.length == 0) {
      return "";
    }
    return array[random.nextInt(array.length)];
  }

  /**
   * Recursively processes a line, replacing all tagged placeholders with random values.
   *
   * @param line the line to process
   * @return the processed line with all tags replaced
   */
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

  /**
   * Generates a single fantasy name.
   *
   * @return a randomly generated fantasy name
   */
  public String generateName() {
    String[] optionsArray = vocab.get("options");
    String template = getRandomElement(optionsArray);
    return processTemplate(template).trim();
  }

  /**
   * Generates a unique name that hasn't been used by any other circle.
   *
   * <p>This method repeatedly calls {@link #generateName()} until it finds a name that
   * doesn't exist in the {@code usedNames} set. Given the high variety of the name generator
   * (hundreds of possible combinations), collisions are extremely rare in practice.
   *
   * <p>As a safety measure, if 1000 attempts are made without finding a unique name,
   * the method will return a generated name with a timestamp suffix to guarantee uniqueness. This
   * fallback scenario is astronomically unlikely under normal usage.
   *
   * @return a unique name that is not present in the {@code usedNames} set
   */
  public String generateUniqueName() {
    for (int attempts = 0; attempts < MAX_UNIQUE_ATTEMPTS; attempts++) {
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

  /**
   * Releases a name back to the pool of available names.
   *
   * @param name the name to release
   * @return true if the name was removed, false if it wasn't in the set
   */
  public boolean releaseName(String name) {
    return usedNames.remove(name);
  }

  /**
   * Adds a name to the used names set.
   *
   * @param name the name to mark as used
   * @return true if the name was added, false if it was already present
   */
  public boolean addName(String name) {
    return usedNames.add(name);
  }

  /**
   * Gets the number of currently used names.
   *
   * @return the count of used names
   */
  public int getUsedNameCount() {
    return usedNames.size();
  }

  /**
   * Checks if a name is currently in use.
   *
   * @param name the name to check
   * @return true if the name is in use, false otherwise
   */
  public boolean isNameUsed(String name) {
    return usedNames.contains(name);
  }

  /**
   * Generates multiple fantasy names.
   *
   * @param count the number of names to generate
   * @return a list of generated names
   */
  public List<String> generateNames(int count) {
    List<String> names = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      names.add(generateName());
    }
    return names;
  }

  /**
   * Generates a list of names from the default template (which produces 5 names).
   *
   * @return a list of 5 generated names
   */
  public List<String> generateNameList() {
    String template = getRandomElement(vocab.get("FIRST"));
    String result = processTemplate(template);
    List<String> names = new ArrayList<>();

    if (result.contains("\n")) {
      String[] nameArray = result.split("\n");
      for (String name : nameArray) {
        String trimmed = name.trim();
        if (!trimmed.isEmpty()) {
          names.add(trimmed);
        }
      }
    } else {
      names.add(result.trim());
    }

    return names;
  }
}