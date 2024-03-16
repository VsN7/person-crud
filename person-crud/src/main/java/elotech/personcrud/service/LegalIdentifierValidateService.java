package elotech.personcrud.service;

import org.springframework.stereotype.Service;

@Service
public class LegalIdentifierValidateService {

    public boolean valid(String legalIdentifier) {

        if (legalIdentifier.length() != 11) {
            return false;
        }

        boolean allDigitsEqual = true;
        for (int i = 1; i < legalIdentifier.length(); i++) {
            if (legalIdentifier.charAt(i) != legalIdentifier.charAt(0)) {
                allDigitsEqual = false;
                break;
            }
        }
        if (allDigitsEqual) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (10 - i) * Character.getNumericValue(legalIdentifier.charAt(i));
        }
        int firstDigitalChecker = 11 - (sum % 11);
        if (firstDigitalChecker > 9) {
            firstDigitalChecker = 0;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (11 - i) * Character.getNumericValue(legalIdentifier.charAt(i));
        }
        int secondDigitalChecker = 11 - (sum % 11);
        if (secondDigitalChecker > 9) {
            secondDigitalChecker = 0;
        }

        return firstDigitalChecker == Character.getNumericValue(legalIdentifier.charAt(9)) &&
                secondDigitalChecker == Character.getNumericValue(legalIdentifier.charAt(10));
    }
}