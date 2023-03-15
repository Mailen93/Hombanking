package com.mindhub.homebanking.Utils;

import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;

public class Utilities {
    public static String returnCvvNumber(){
        int number = (int) (Math.random() * 999);
       String completeNumber = String.format("%03d", number);
        return completeNumber;}

    public static String randomNumberCard(CardRepository cardRepository){
        String codString;
        Boolean cardBoolean;
        do {
            codString= randomString();
            cardBoolean= cardRepository.existsCardByNumber(codString);
        }while(cardBoolean);
        return codString;}

    public static String randomString(){
        int number1_1 = (int) (Math.random() * (5 - 4 + 1) + 4);
        int number1 = (int) ((Math.random() * (999 - 100) + 1) + 100);
        int number2 = (int) (Math.random() * (9999 - 1000  + 1) + 1000);
        int number3 = (int) (Math.random() * (9999 - 1000  + 1) + 1000);
        int number4 = (int) (Math.random() * (9999 - 1000  + 1) + 1000);
        String number = number1_1+""+number1+"-"+number2+"-"+number3+"-"+number4;
        return number;}

    public static String GenerateNumber(){
        int number1=(int) (Math.random() * (99999999));  //(INT) Fuerza que Math.random() sea entero y no double
        String number ="VIN-"+number1;
        return number;}

    public static String Number(AccountRepository accountRepository){
        String Number;
        boolean verifyNumber;
        do {
            Number=GenerateNumber();
            verifyNumber=accountRepository.existsByNumber(Number);
        }while(verifyNumber);
        return Number;}
}
