/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hello;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void homePage() throws Exception {
        mockMvc.perform(get("/index.html"))
               .andExpect(content().string(containsString("Avro schema convert to Elasticsearch Mapping")));
    }

    @Test
    public void convertingRequest() throws Exception {
        final MockHttpServletRequestBuilder convertingRequest = post("/avro/convert/schema/to/es");
        String newLine = System.lineSeparator();
        convertingRequest.content("{" + newLine
                                  + "   \"type\" : \"record\"," + newLine
                                  + "   \"namespace\" : \"Tutorialspoint\"," + newLine
                                  + "   \"name\" : \"Employee\"," + newLine
                                  + "   \"fields\" : [" + newLine
                                  + "      { \"name\" : \"Name\" , \"type\" : \"string\" }," + newLine
                                  + "      { \"name\" : \"Age\" , \"type\" : \"int\" }" + newLine
                                  + "   ]" + newLine
                                  + "}");
        mockMvc.perform(convertingRequest)
               .andExpect(content().string(containsString("{" + newLine
                                                          + "  \"mappings\":{" + newLine
                                                          + "    \"doc\":{" + newLine
                                                          + "      \"dynamic\":\"true\"," + newLine
                                                          + "      \"numeric_detection\":true," + newLine
                                                          + "      \"properties\":{" + newLine
                                                          + "        \"Name\":{" + newLine
                                                          + "          \"type\":\"keyword\"," + newLine
                                                          + "          \"ignore_above\":256," + newLine
                                                          + "          \"norms\":false," + newLine
                                                          + "          \"index_options\":\"freqs\"" + newLine
                                                          + "        }," + newLine
                                                          + "        \"Age\":{" + newLine
                                                          + "          \"type\":\"integer\"" + newLine
                                                          + "        }" + newLine
                                                          + "      }" + newLine
                                                          + "    }" + newLine
                                                          + "  }" + newLine
                                                          + "}")));
    }
}
