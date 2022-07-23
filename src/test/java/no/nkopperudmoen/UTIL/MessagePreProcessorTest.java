package no.nkopperudmoen.UTIL;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessagePreProcessorTest {
    @Test
    public void convertToDays() {
        int min = 1535;
        //1 dag, 1 time og 35 min
        String formattedTime = MessagePreProcessor.formatTimeFromMinutes(min);
        assertEquals("1 dag 1 time 35 min", formattedTime);
        formattedTime = MessagePreProcessor.formatTimeFromMinutes(1440);
        assertEquals("1 dag", formattedTime);
    }

    @Test
    public void convertToHours() {
        int min = 1400;
        String formattedTime = MessagePreProcessor.formatTimeFromMinutes(min);
        assertEquals(" 23 timer 20 min", formattedTime);
        formattedTime = MessagePreProcessor.formatTimeFromMinutes(120);
        assertEquals(" 2 timer", formattedTime);
    }

    @Test
    public void convertToMin() {
        String formattedTime = MessagePreProcessor.formatTimeFromMinutes(55);
        assertEquals(" 55 min", formattedTime);

    }

}