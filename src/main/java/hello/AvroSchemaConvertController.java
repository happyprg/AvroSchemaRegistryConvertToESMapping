package hello;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/avro")
public class AvroSchemaConvertController {

    @RequestMapping(value = "/load/schema", method = RequestMethod.GET)
    public @ResponseBody
    String loadFromSchemaUrl(@ModelAttribute AvroSchema avroSchema) throws IOException, JSONException {
        if (StringUtils.isNotBlank(avroSchema.getUrl())) {
            return StringEscapeUtils.unescapeJson(
                    AvroSchema.loadFromSchemaUrl(avroSchema.getUrl()).getString("schema"));
        }
        return StringUtils.EMPTY;
    }

    @RequestMapping(value = "/convert/schema/to/es", method = RequestMethod.POST)
    public @ResponseBody
    String greeting(@RequestBody String avroSchema) throws IOException, JSONException {
        final String decode = URLDecoder.decode(avroSchema, "UTF-8");
        if (StringUtils.isNotBlank(decode)) {
            return AvroSchema.convertToESMapping(decode);
        }
        return StringUtils.EMPTY;
    }

}
