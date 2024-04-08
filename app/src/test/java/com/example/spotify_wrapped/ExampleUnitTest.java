package com.example.spotify_wrapped;

import org.json.JSONException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_llmQuery() throws JSONException {
        LLMQueryManager manager = new LLMQueryManager();
        manager.queryPrompt("I love listening to Drake");
    }
}