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
        convertingRequest.content("{\n"
                                  + "   \"type\" : \"record\",\n"
                                  + "   \"namespace\" : \"Tutorialspoint\",\n"
                                  + "   \"name\" : \"Employee\",\n"
                                  + "   \"fields\" : [\n"
                                  + "      { \"name\" : \"Name\" , \"type\" : \"string\" },\n"
                                  + "      { \"name\" : \"Age\" , \"type\" : \"int\" }\n"
                                  + "   ]\n"
                                  + "}");
        mockMvc.perform(convertingRequest)
               .andExpect(content().string(containsString("{\n"
                                                          + "  \"mappings\":{\n"
                                                          + "    \"doc\":{\n"
                                                          + "      \"dynamic\":\"true\",\n"
                                                          + "      \"numeric_detection\":true,\n"
                                                          + "      \"properties\":{\n"
                                                          + "        \"Name\":{\n"
                                                          + "          \"type\":\"keyword\",\n"
                                                          + "          \"ignore_above\":256,\n"
                                                          + "          \"norms\":false,\n"
                                                          + "          \"index_options\":\"freqs\"\n"
                                                          + "        },\n"
                                                          + "        \"Age\":{\n"
                                                          + "          \"type\":\"integer\"\n"
                                                          + "        }\n"
                                                          + "      }\n"
                                                          + "    }\n"
                                                          + "  }\n"
                                                          + "}")));
    }
}
