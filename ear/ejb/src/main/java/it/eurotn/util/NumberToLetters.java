package it.eurotn.util;

/**
 *
 * Traduce un numero nella rappresentazione letterale.
 *
 * @author giangi
 * @version 1.0, 01/ott/2014
 *
 */
public final class NumberToLetters {

  /**
   *
   * @param importo
   *          importo da convertire
   * @return testo dell'importo
   */
  public static String numberToText(double importo) {
    // metodo wrapper
    int number = (int) Math.round(importo * 100);
    int parteIntera = number / 100;
    String parteDecimale = String.format("%02d", number % 100);

    if (parteIntera == 0) {
      return "zero/" + parteDecimale;
    } else {
      return numberToTextRicorsiva(parteIntera) + "/" + parteDecimale;
    }
  }

  /**
   *
   * @param number
   *          importo da convertire
   * @return testo dell'importo
   */
  public static String numberToText(int number) {
    // metodo wrapper
    if (number == 0) {
      return "zero";
    } else {
      return numberToTextRicorsiva(number);
    }
  }

  /**
   *
   * @param importo
   *          importo da convertire
   * @return testo dell'importo
   */
  private static String numberToTextRicorsiva(int number) {
    if (number < 0) {
      return "meno " + numberToTextRicorsiva(-number);
    } else if (number == 0) {
      return "";
    } else if (number <= 19) {
      return new String[] { "uno", "due", "tre", "quattro", "cinque", "sei", "sette", "otto",
          "nove", "dieci", "undici", "dodici", "tredici", "quattordici", "quindici", "sedici",
          "diciassette", "diciotto", "diciannove" }[number - 1];
    } else if (number <= 99) {
      String[] vettore = { "venti", "trenta", "quaranta", "cinquanta", "sessanta", "settanta",
          "ottanta", "novanta" };
      String letter = vettore[number / 10 - 2];
      int tt = number % 10; // t è la prima cifra di n
      // se è 1 o 8 va tolta la vocale finale di letter
      if (tt == 1 || tt == 8) {
        letter = letter.substring(0, letter.length() - 1);
      }
      return letter + numberToTextRicorsiva(number % 10);
    } else if (number <= 199) {
      return "cento" + numberToTextRicorsiva(number % 100);
    } else if (number <= 999) {
      int molt = number % 100;
      molt /= 10; // divisione intera per 10 della variabile
      String letter = "cent";
      if (molt != 8) {
        letter = letter + "o";
      }
      return numberToTextRicorsiva(number / 100) + letter + numberToTextRicorsiva(number % 100);
    } else if (number <= 1999) {
      return "mille" + numberToTextRicorsiva(number % 1000);
    } else if (number <= 999999) {
      return numberToTextRicorsiva(number / 1000) + "mila" + numberToTextRicorsiva(number % 1000);
    } else if (number <= 1999999) {
      return "unmilione" + numberToTextRicorsiva(number % 1000000);
    } else if (number <= 999999999) {
      return numberToTextRicorsiva(number / 1000000) + "milioni"
          + numberToTextRicorsiva(number % 1000000);
    } else if (number <= 1999999999) {
      return "unmiliardo" + numberToTextRicorsiva(number % 1000000000);
    } else {
      return numberToTextRicorsiva(number / 1000000000) + "miliardi"
          + numberToTextRicorsiva(number % 1000000000);
    }
  }

  private NumberToLetters() {

  }
}