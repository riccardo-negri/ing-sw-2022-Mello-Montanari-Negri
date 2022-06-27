package it.polimi.ingsw.model.entity;

import com.google.gson.*;
import it.polimi.ingsw.model.entity.characters.*;
import it.polimi.ingsw.model.entity.characters.Character;
import it.polimi.ingsw.model.entity.game_state.*;

import java.lang.reflect.Type;

public class JsonDeserializerClass {
    private JsonDeserializerClass(){}

    private static Gson gson;

    public static void setGson(Gson gson) { JsonDeserializerClass.gson = gson; }

    public static Gson getGson() { return gson; }

    public static class CharacterDeserializer implements JsonDeserializer<Character> {

        /**
         * gson deserialization method for character class, using the id as deserialization criteria
         * @return the deserialized character
         * @throws JsonParseException error in the deserialization process
         */
        @Override
        public Character deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return switch (jsonElement.getAsJsonObject().get("characterId").getAsInt()) {
                case 1 -> gson.fromJson(jsonElement, CharacterOne.class);
                case 2 -> gson.fromJson(jsonElement, CharacterTwo.class);
                case 3 -> gson.fromJson(jsonElement, CharacterThree.class);
                case 4 -> gson.fromJson(jsonElement, CharacterFour.class);
                case 5 -> gson.fromJson(jsonElement, CharacterFive.class);
                case 6 -> gson.fromJson(jsonElement, CharacterSix.class);
                case 7 -> gson.fromJson(jsonElement, CharacterSeven.class);
                case 8 -> gson.fromJson(jsonElement, CharacterEight.class);
                case 9 -> gson.fromJson(jsonElement, CharacterNine.class);
                case 10 -> gson.fromJson(jsonElement, CharacterTen.class);
                case 11 -> gson.fromJson(jsonElement, CharacterEleven.class);
                case 12 -> gson.fromJson(jsonElement, CharacterTwelve.class);
                default -> null;
            };
        }
    }

    public static class GameStateDeserializer implements JsonDeserializer<GameState> {

        /**
         * Deserialization method for gameState, using the game state string as criteria
         * @return the deserialized game state subclass
         * @throws JsonParseException an error in the deserialization process
         */
        @Override
        public GameState deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return switch (jsonElement.getAsJsonObject().get("gameState").getAsString()) {
                case "PS" -> gson.fromJson(jsonElement, PlanningState.class);
                case "MSS" -> gson.fromJson(jsonElement, MoveStudentActionState.class);
                case "MMNS" -> gson.fromJson(jsonElement, MoveMotherNatureActionState.class);
                case "CCS" -> gson.fromJson(jsonElement, ChooseCloudActionState.class);
                default -> null;
            };
        }
    }


}
