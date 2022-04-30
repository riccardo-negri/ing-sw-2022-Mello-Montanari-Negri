package it.polimi.ingsw.model.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.enums.*;
import it.polimi.ingsw.model.entity.bag.Bag;
import it.polimi.ingsw.model.entity.bag.ServerBag;
import it.polimi.ingsw.model.entity.characters.Character;
import it.polimi.ingsw.model.entity.gameState.GameState;
import it.polimi.ingsw.model.entity.gameState.PlanningState;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Main class of the single game, with a factory method to create a new game
 * The objects in the class are saved into a static list, and can be retrieved from their id
 */
public class Game implements Serializable {

    private static Gson serializationGson, deserializationGson;
    private static Integer idCount = 0;
    private static List<Game> gameEntities;

    private final Integer id;
    private final GameMode gameMode;
    private final PlayerNumber playerNumber;
    private final List<Wizard> wizardList;
    private final Professor[] professors;
    private final List<IslandGroup> islandGroupList;
    private final List<Cloud> cloudList;
    private final Character[] characters;
    private final Bag bag;

    private GameState gameState;

    /**
     * Generates all the components of the game
     * @param id id of the new created game, to get it
     * @param gameMode easy or complete, to activate the character cards
     * @param playerNumber number of players
     */
    private Game(Integer id, GameMode gameMode, PlayerNumber playerNumber, Type type) {
        this.id = id;
        this.gameMode = gameMode;
        this.playerNumber = playerNumber;

        Random randomGenerator = new Random();
        this.bag = new ServerBag(randomGenerator);

        if(gameMode == GameMode.COMPLETE) {
            characters = new Character[3];
            for(int i=0; i<3; i++) {
                boolean flag = true;
                while(flag) {
                    flag = false;
                    characters[i] = Character.generateCharacter(id, randomGenerator.nextInt(12), bag);
                    for(int j=0; j<i; j++) if (characters[i].equals(characters[j])) {
                        flag = true;
                        break;
                    }
                }
            }
        } else {
            characters = null;
        }

        this.professors = new Professor[5];
        for (int i=0; i<5; i++) {
            professors[i] = new Professor(id, StudentColor.fromNumber(i));
        }

        islandGroupList = new LinkedList<>();
        for(int i=0; i<12; i++) {
            try {
                islandGroupList.add(new IslandGroup(bag.requestStudents((i == 0 || i == 6) ? 0 : 1),i));
            } catch (Exception e) { System.err.println("Error creating island group"); }
        }

        cloudList = new ArrayList<>();
        for(int i = 0; i<playerNumber.getWizardNumber(); i++) {
            cloudList.add(new Cloud(i, bag, this.playerNumber));
        }

        wizardList = new ArrayList<>();
        for (int i=0; i<playerNumber.getWizardNumber(); i++) {
            try{
                wizardList.add(new Wizard(bag.requestStudents(playerNumber.getEntranceNumber()), Tower.fromNumber(i), playerNumber.getTowerNumber()));
            } catch (Exception e) { System.err.println(e.getMessage()); }
        }

        this.gameState = new PlanningState(id, wizardList.stream().map(x -> x.getTowerColor()).collect(Collectors.toList()), randomGenerator);

    }

    /**
     * Factory method to create a new game
     * @param gameMode easy or complete mode
     * @param playerNumber two, three or four players
     * @return Returns the id of the created match
     */
    public static Integer gameEntityFactory(GameMode gameMode, PlayerNumber playerNumber, Type type) {
        if (gameEntities == null) gameEntities = new ArrayList<>();
        Game game = new Game(idCount, gameMode, playerNumber, type);
        gameEntities.add(game);
        return idCount++;
    }

    public String serializeGame() {
        if (serializationGson == null) serializationGson = new Gson();
        return serializationGson.toJson(this, Game.class);
    }

    /**
     * Method to serialize a game, and save it to the disk
     * @return string of the serialization
     * @throws Exception if the directory can't be reached
     */
    public String serializeGame(String filename) throws Exception {
        if (serializationGson == null) serializationGson = new Gson();
        String out = serializationGson.toJson(this, Game.class);
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(out);
        writer.close();
        return out;
    }

    /**
     * Method to deserialize a game from a file
     * @param fileName name of the file in the disk
     * @return the index of the game
     * @throws Exception if the file can't be found, or the index of the game already exists
     */
    public static Integer deserializeGame(String fileName) throws Exception {
        if (deserializationGson == null) initializeDeserializationGson();

        if (gameEntities == null) gameEntities = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String in = reader.readLine();
        reader.close();

        Game newGame = JsonDeserializerClass.getGson().fromJson(in, Game.class);
        if(gameEntities.stream().filter(x -> x.isGameId(newGame.id)).count() != 0) throw new Exception("Game already present");
        newGame.getCloudList().stream().forEach(x -> x.setBag(newGame.bag));
        Game.gameEntities.add(newGame);
        return newGame.id;
    }

    private static void initializeDeserializationGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        JsonDeserializerClass.BagDeserializer bagDeserializer = new JsonDeserializerClass.BagDeserializer();
        gsonBuilder.registerTypeAdapter(Bag.class, bagDeserializer);
        JsonDeserializerClass.CharacterDeserializer characterDeserializer = new JsonDeserializerClass.CharacterDeserializer();
        gsonBuilder.registerTypeAdapter(Character.class, characterDeserializer);
        JsonDeserializerClass.GameStateDeserializer gameStateDeserializer = new JsonDeserializerClass.GameStateDeserializer();
        gsonBuilder.registerTypeAdapter(GameState.class, gameStateDeserializer);
        deserializationGson = gsonBuilder.create();
        JsonDeserializerClass.setGson(deserializationGson);
    }

    /**
     * Method to retrieve the game object from the game id
     * @param gameId game id to retrieve
     * @return game object with id
     * @throws MissingResourceException if id missing
     */
    public static Game request (Integer gameId) throws MissingResourceException, IllegalStateException {
        List <Game> result = gameEntities.stream().filter(x -> x.isGameId(gameId)).collect(Collectors.toList());
        if (result.isEmpty()) throw new MissingResourceException("Game not found", "GameStateEentity", gameId.toString());
        return result.get(0);
    }

    /**
     * class to move mother nature
     * @param steps number of steps to be done
     */
    public void doMotherNatureSteps(Integer steps) {
        for (int i = 0; i < steps; i++) {
            islandGroupList.add(islandGroupList.remove(0));
        }
    }

    /**
     * checks if some island groups need to be united
     */
    public void unifyIslands() {
        for (int i=0; i<islandGroupList.size(); i++) {
            if (islandGroupList.get(i).getTower() == islandGroupList.get((i+1)%islandGroupList.size()).getTower()) {
                islandGroupList.get(i).getIslandList().addAll(islandGroupList.get((i+1)%islandGroupList.size()).getIslandList());
                islandGroupList.remove((i+1)%islandGroupList.size());
                i--;
            }
        }
    }

    public Wizard getWizard(Tower tower) {
        return wizardList.stream().filter(w -> w.getTowerColor() == tower).findFirst().get();
    }

    public Island getIsland(Integer islandId) {
        return islandGroupList.stream().flatMap(x -> x.getIslandList().stream()).filter(x -> x.getId()==islandId).findFirst().get();
    }

    public Cloud getCloud(Integer cloudId) {
        return cloudList.stream().filter(x -> x.getId()==cloudId).findFirst().get();
    }

    public IslandGroup getIslandGroup(Integer islandGroupId) {
        return islandGroupList.stream().filter(x -> x.getId()==islandGroupId).findFirst().get();
    }

    public IslandGroup getFistIslandGroup () { return islandGroupList.get(0); }

    public Professor getProfessor(StudentColor studentColor) { return professors[studentColor.getValue()]; }

    public void updateGameState(GameState newState) { gameState = newState; }

    public PlayerNumber getPlayerNumber() { return playerNumber; }

    public List<IslandGroup> getIslandGroupList() { return islandGroupList; }

    public List<Cloud> getCloudList() { return cloudList; }

    private boolean isGameId(Integer id) { return id.equals(this.id); }

    public GameState getGameState() { return gameState; }

    public Character getCharacter(int index) { return characters[index]; }
}
