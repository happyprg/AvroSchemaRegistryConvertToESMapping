package hello;

import static org.apache.avro.Schema.Type.INT;
import static org.apache.avro.Schema.Type.STRING;
import static org.apache.avro.Schema.Type.UNION;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.Schema.Parser;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.cedarsoftware.util.io.JsonWriter;

public class AvroSchema {

    private String data;
    private String url;

    public static String normalizeDataTypeToES(String lookupType) {
        if (lookupType.compareToIgnoreCase(INT.name()) == 0) {
            lookupType = "integer";
        } else if (lookupType.compareToIgnoreCase(STRING.name()) == 0) {
            lookupType = "keyword";
        }

        if (StringUtils.containsAny(lookupType.toLowerCase()
                , "integer"
                , "long"
                , "double"
                , "float"
                , "boolean"
                , "keyword")
            == false) {
            return null;
        }

        return lookupType;
    }

    public static JSONObject loadFromSchemaUrl(String parsingUrl) throws JSONException, IOException {
        return new JSONObject(IOUtils.toString(new URL(parsingUrl), Charset.forName("UTF-8")));
    }

    public static String convertToESMapping(String avroSchemaJson) throws IOException {
        final Schema avroSchema = new Parser().parse(avroSchemaJson);
        final List<Field> fields = avroSchema.getFields();
        XContentBuilder builder = XContentFactory.jsonBuilder();

        builder.startObject();
        {
            builder.startObject("mappings");
            {

                builder.startObject("doc");
                {
                    builder.field("dynamic", "true");
                    builder.field("numeric_detection", true);

                    builder.startObject("properties");
                    {
                        for (Field field : fields) {
                            String lookupType = field.schema().getType().getName();
                            if (field.schema().getType() == UNION && field.schema().getTypes().size() > 1) {
                                lookupType = field.schema().getTypes().get(1).getType().getName();
                            }
                            final String normalizeDataType = normalizeDataTypeToES(lookupType);
                            if (normalizeDataType == null) {
                                System.out.println(
                                        "could not handling this type of field - " + field + ", lookupType - " + lookupType);
                                continue;
                            }

                            builder.startObject(field.name());
                            {
                                builder.field("type", normalizeDataType);
                                if (normalizeDataType.equals("keyword")) {
                                    //for performance tuning
                                    builder.field("ignore_above", 256);
                                    builder.field("norms", false);
                                    builder.field("index_options", "freqs");
                                }
                            }
                            builder.endObject();
                        }
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();

        }
        builder.endObject();
        builder.close();
        return JsonWriter.formatJson(builder.string());
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

