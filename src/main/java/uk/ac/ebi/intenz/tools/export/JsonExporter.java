package uk.ac.ebi.intenz.tools.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import uk.ac.ebi.intenz.domain.constants.View;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeClass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass;
import uk.ac.ebi.xchars.SpecialCharacters;

/**
 * Utility class to export enzyme entries - but also classes, subclasses and
 * subsubclasses - as JSON.
 * @author rafa
 * @since 1.3.18
 */
public class JsonExporter implements IntenzExporter {

    public void export(Collection<EnzymeEntry> enzymes, OutputStream os)
    throws IOException {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (EnzymeEntry enzyme : enzymes) {
            jsonArrayBuilder.add(jsonFromEnzyme(enzyme));
        }
        JsonWriter jsonWriter = Json.createWriter(os);
        jsonWriter.writeArray(jsonArrayBuilder.build());
        jsonWriter.close();
    }

    public void export(EnzymeEntry enzyme, OutputStream os) throws IOException {
        JsonWriter jsonWriter = Json.createWriter(os);
        jsonWriter.writeObject(jsonFromEnzyme(enzyme));
        jsonWriter.close();
    }

    /**
     * Exports an EC sub-subclass as JSON, including only its <i>active</i> EC
     * numbers (not deleted, not transferred).
     * @param essc The EC sub-subclass.
     * @param os The output stream to write to.
     */
    public void export(EnzymeSubSubclass essc, OutputStream os){
        export(essc, os, true);
    }

    /**
     * Exports an EC sub-subclass as JSON.
     * @param essc The EC sub-subclass.
     * @param os The output stram to write to.
     * @param onlyActive Do we want to export only the <i>active</i> (not
     *      deleted, not transferred) EC numbers?
     */
    public void export(EnzymeSubSubclass essc, OutputStream os,
            boolean onlyActive){
        JsonWriter jsonWriter = Json.createWriter(os);
        jsonWriter.writeObject(jsonFromSubsubClass(essc, onlyActive));
        jsonWriter.close();
    }

    /**
     * Exports an EC subclass as JSON.
     * @param esc The EC subclass.
     * @param os The output stream to write to.
     */
    public void export(EnzymeSubclass esc, OutputStream os){
        JsonWriter jsonWriter = Json.createWriter(os);
        jsonWriter.writeObject(jsonFromSubclass(esc));
        jsonWriter.close();
    }

    /**
     * Exports an EC class as JSON.
     * @param ec The EC class.
     * @param os The output stream to write to.
     */
    public void export(EnzymeClass ec, OutputStream os){
        JsonWriter jsonWriter = Json.createWriter(os);
        jsonWriter.writeObject(jsonFromClass(ec));
        jsonWriter.close();
    }

    /**
     * Turns a list of EC classes into a JSON array.
     * @param ecs The EC classes.
     * @param os The output stream to write to.
     */
    public void export(List<EnzymeClass> ecs, OutputStream os){
        JsonWriter jsonWriter = Json.createWriter(os);
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (EnzymeClass ec : ecs) {
            jsonArrayBuilder.add(jsonFromClass(ec));
        }
        jsonWriter.writeArray(jsonArrayBuilder.build());
        jsonWriter.close();
    }

    /**
     * Turns an EC entry into a JSON object. The included properties are:
     * <ul>
     *  <li>ec (string)</li>
     *  <li>name (string)</li>
     *  <li>synonyms (array of string)</li>
     * </ul>
     * @param enzyme
     * @return a JSON object
     */
    private JsonObject jsonFromEnzyme(EnzymeEntry enzyme) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("ec", enzyme.getEc().toString());
        jsonObjectBuilder.add("name", SpecialCharacters.getInstance(null)
                .xml2Display(enzyme.getCommonName().getName()));
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (EnzymeName synonym : enzyme.getSynonyms(View.INTENZ)){
            jsonArrayBuilder.add(SpecialCharacters.getInstance(null)
                    .xml2Display(synonym.getName()));
        }
        jsonObjectBuilder.add("synonyms", jsonArrayBuilder);
        // ...
        return jsonObjectBuilder.build();
    }

    /**
     * Turns an EC sub-subclass into a JSON object. The included properties are:
     * <ul>
     *  <li>ec (string)</li>
     *  <li>name (string)</li>
     *  <li>description (string)</li>
     *  <li>entries (array of JSON enzyme objects, see {@link
     *      #jsonFromEnzyme(uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry)})</li>
     * </ul>
     * @param essc the EC sub-subclass.
     * @param onlyActive Do we want to include only the active EC numbers
     * @return a JSON object.
     */
    private JsonObject jsonFromSubsubClass(EnzymeSubSubclass essc,
            boolean onlyActive){
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("ec", essc.getEc().toString());
        jsonObjectBuilder.add("name", SpecialCharacters.getInstance(null)
                .xml2Display(essc.getClassName()));
        jsonObjectBuilder.add("description", SpecialCharacters.getInstance(null)
                .xml2Display(essc.getDescription()));
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (EnzymeEntry e : essc.getEntries()) {
            if (onlyActive && !e.isActive()) continue;
            jsonArrayBuilder.add(jsonFromEnzyme(e));
        }
        jsonObjectBuilder.add("entries", jsonArrayBuilder.build());
        return jsonObjectBuilder.build();
    }

    /**
     * Turns an EC subclass into a JSON object. The included properties are:
     * <ul>
     *  <li>ec (string)</li>
     *  <li>name (string)</li>
     *  <li>description (string)</li>
     *  <li>subsubclasses (array of JSON objects with just ec, name and
     *      description).</li>
     * </ul>
     * @param esc The EC subclass.
     * @return a JSON object.
     */
    private JsonObject jsonFromSubclass(EnzymeSubclass esc){
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("ec", esc.getEc().toString());
        jsonObjectBuilder.add("name", SpecialCharacters.getInstance(null)
                .xml2Display(esc.getName()));
        jsonObjectBuilder.add("description", SpecialCharacters.getInstance(null)
                .xml2Display(esc.getDescription()));
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (EnzymeSubSubclass essc : esc.getSubSubclasses()) {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                    .add("ec", essc.getEc().toString())
                    .add("name", SpecialCharacters.getInstance(null)
                            .xml2Display(essc.getName()))
                    .add("description", SpecialCharacters.getInstance(null)
                            .xml2Display(essc.getDescription())));
        }
        jsonObjectBuilder.add("subsubclasses", jsonArrayBuilder.build());
        return jsonObjectBuilder.build();
    }

    /**
     * Turns an EC class into a JSON object. The included properties are:
     * <ul>
     *  <li>ec (string)</li>
     *  <li>name (string)</li>
     *  <li>description (string)</li>
     *  <li>subclasses (array of JSON objects with just ec, name and
     *      description).</li>
     * </ul>
     * @param ec the EC class.
     * @return a JSON object.
     */
    private JsonObject jsonFromClass(EnzymeClass ec){
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("ec", ec.getEc().toString());
        jsonObjectBuilder.add("name", SpecialCharacters.getInstance(null)
                .xml2Display(ec.getName()));
        jsonObjectBuilder.add("description", SpecialCharacters.getInstance(null)
                .xml2Display(ec.getDescription()));
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (EnzymeSubclass esc : ec.getSubclasses()) {
            jsonArrayBuilder.add(Json.createObjectBuilder()
                .add("ec", esc.getEc().toString())
                .add("name", SpecialCharacters.getInstance(null)
                        .xml2Display(esc.getName()))
                .add("description", SpecialCharacters.getInstance(null)
                        .xml2Display(esc.getDescription())));
        }
        jsonObjectBuilder.add("subclasses", jsonArrayBuilder.build());
        return jsonObjectBuilder.build();
    }
}
