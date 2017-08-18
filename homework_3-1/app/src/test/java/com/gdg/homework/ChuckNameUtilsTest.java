package com.gdg.homework;

import android.support.v4.util.Pair;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by vkirillov on 18.08.2017.
 */
public class ChuckNameUtilsTest {
    private List<Pair<String, String>> mTestCases = new ArrayList<>();

    public ChuckNameUtilsTest() {
        mTestCases.add(new Pair<>(
                "Nagasaki never had a bomb dropped on it. Chuck Norris jumped out of a plane and punched the ground",
                "Nagasaki never had a bomb dropped on it. Vitaly Kirillov jumped out of a plane and punched the ground"
        ));
        mTestCases.add(new Pair<>(
                "Chuck Norris can slam a revolving door.",
                "Vitaly Kirillov can slam a revolving door."
        ));
        mTestCases.add(new Pair<>(
                "There is in fact an 'I' in Norris, but there is no 'team'. Not even close.",
                "There is in fact an 'I' in Kirillov, but there is no 'team'. Not even close."
        ));
        mTestCases.add(new Pair<>(
                "Someone once videotaped Chuck Norris getting pissed off. It was called Walker: Texas Chain Saw Masacre.",
                "Someone once videotaped Vitaly Kirillov getting pissed off. It was called Walker: Texas Chain Saw Masacre."
        ));
        mTestCases.add(new Pair<>(
                "The Manhattan Project was not intended to create nuclear weapons, it was meant to recreate the destructive power in a Chuck Norris Roundhouse Kick. They didn't even come close.",
                "The Manhattan Project was not intended to create nuclear weapons, it was meant to recreate the destructive power in a Vitaly Kirillov Roundhouse Kick. They didn't even come close."
        ));
        mTestCases.add(new Pair<>(
                "No one has ever pair-programmed with Chuck Norris and lived to tell about it.",
                "No one has ever pair-programmed with Vitaly Kirillov and lived to tell about it."
        ));
        mTestCases.add(new Pair<>(
                "In the medical community, death is referred to as &quot;Chuck Norris Disease&quot;",
                "In the medical community, death is referred to as &quot;Vitaly Kirillov Disease&quot;"
        ));
        mTestCases.add(new Pair<>(
                "Chuck",
                "Vitaly"
        ));
        mTestCases.add(new Pair<>(
                "Norris",
                "Kirillov"
        ));
    }

    @Test
    public void replaceChuckName() throws Exception {
        for (Pair<String, String> pair : mTestCases) {
            Assert.assertEquals(pair.second, ChuckNameUtils.replaceChuckName(pair.first));
        }
    }

}