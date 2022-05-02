package it.polimi.ingsw.model.entity;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.entity.bag.Bag;
import it.polimi.ingsw.model.entity.bag.ClientBag;
import it.polimi.ingsw.model.entity.bag.ServerBag;
import it.polimi.ingsw.model.entity.characters.*;
import it.polimi.ingsw.model.entity.characters.Character;
import it.polimi.ingsw.model.entity.gameState.*;
import it.polimi.ingsw.model.enums.StudentColor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonDeserializerClass {

    private static Gson gson;

    public static void setGson(Gson gson) { JsonDeserializerClass.gson = gson; }

    public static Gson getGson() { return gson; }

    public static class BagDeserializer implements JsonDeserializer<Bag> {

        @Override
        public Bag deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            switch (jsonElement.getAsJsonObject().get("type").getAsString()) {
                case "SERVER": return gson.fromJson(jsonElement, ServerBag.class);
                case "CLIENT": return gson.fromJson(jsonElement, ClientBag.class);
                default: return null;
            }
        }
    }

    public static class CharacterDeserializer implements JsonDeserializer<Character> {

        @Override
        public Character deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            switch (jsonElement.getAsJsonObject().get("characterId").getAsInt()) {
                case 1: return gson.fromJson(jsonElement, CharacterOne.class);
                case 2: return gson.fromJson(jsonElement, CharacterTwo.class);
                case 3: return gson.fromJson(jsonElement, CharacterThree.class);
                case 4: return gson.fromJson(jsonElement, CharacterFour.class);
                case 5: return gson.fromJson(jsonElement, CharacterFive.class);
                case 6: return gson.fromJson(jsonElement, CharacterSix.class);
                case 7: return gson.fromJson(jsonElement, CharacterSeven.class);
                case 8: return gson.fromJson(jsonElement, CharacterEight.class);
                case 9: return gson.fromJson(jsonElement, CharacterNine.class);
                case 10: return gson.fromJson(jsonElement, CharacterTen.class);
                case 11: return gson.fromJson(jsonElement, CharacterEleven.class);
                case 12: return gson.fromJson(jsonElement, CharacterTwelve.class);
                default: return null;
            }
        }
    }

    public static class GameStateDeserializer implements JsonDeserializer<GameState> {

        @Override
        public GameState deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            switch (jsonElement.getAsJsonObject().get("gameState").getAsString()) {
                case "PS": return gson.fromJson(jsonElement, PlanningState.class);
                case "MSS": return gson.fromJson(jsonElement, MoveStudentActionState.class);
                case "MMNS": return gson.fromJson(jsonElement, MoveMotherNatureActionState.class);
                case "CIS": return gson.fromJson(jsonElement, ChooseCloudActionState.class);
                default: return null;
            }
        }
    }


}