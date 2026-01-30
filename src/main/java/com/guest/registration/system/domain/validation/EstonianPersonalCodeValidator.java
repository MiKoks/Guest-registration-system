package com.guest.registration.system.domain.validation;

public final class EstonianPersonalCodeValidator {

  public static boolean isValid(String code) {
    if (code == null || !code.matches("\\d{11}")) return false;

    int[] digits = code.chars().map(c -> c - '0').toArray();
    int check = digits[10];

    int[] w1 = {1,2,3,4,5,6,7,8,9,1};
    int[] w2 = {3,4,5,6,7,8,9,1,2,3};

    int mod1 = weightedMod(digits, w1);
    if (mod1 != 10) return mod1 == check;

    int mod2 = weightedMod(digits, w2);
    if (mod2 != 10) return mod2 == check;

    return check == 0;
  }

  private static int weightedMod(int[] digits, int[] weights) {
    int sum = 0;
    for (int i = 0; i < 10; i++) sum += digits[i] * weights[i];
    return sum % 11;
  }

  private EstonianPersonalCodeValidator() {}
}
