package it.polimi.ingsw.model.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.entity.characters.CharacterEleven;
import it.polimi.ingsw.model.entity.characters.CharacterOne;
import it.polimi.ingsw.model.enums.*;
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
public class Game {

    private static Gson serializationGson, deserializationGson;
    private static Integer idCount = 0;
    private static List<Game> gameEntities;

    private transient Integer id;
    private final GameMode gameMode;
    private final PlayerNumber playerNumber;
    private List<Wizard> wizardList;
    private Professor[] professors;
    private List<IslandGroup> islandGroupList;
    private List<Cloud> cloudList;
    private Character[] characters;
    private Bag bag;

    private GameState gameState;
    private boolean gameEnded;
    private Tower winner;

    /**
     * Generates all the components of the game
     * @param id id of the new created game, to get it
     * @param gameMode easy or complete, to activate the character cards
     * @param playerNumber number of players
     */
    private Game(Integer id, GameMode gameMode, PlayerNumber playerNumber) {
        this.id = id;
        this.gameMode = gameMode;
        this.playerNumber = playerNumber;
    }

    /**
     * Launched after the constructor, helps generate the game
     */
    private void initializeGame() {
        Random randomGenerator = new Random();
        this.bag = new Bag(randomGenerator);

        islandGroupList = new LinkedList<>();
        Iterator<StudentColor> studentsIterator = bag.requestStartingBoard().listIterator();
        for(int i=0; i<12; i++) {
            if (i != 0 && i != 6) {
                List<StudentColor> list = new ArrayList<>();
                list.add(studentsIterator.next());
                islandGroupList.add(new IslandGroup(list, i));
            } else
                islandGroupList.add(new IslandGroup(new ArrayList<>(), i));
        }

        if(gameMode == GameMode.COMPLETE) {
            characters = new Character[3];
            for(int i=0; i<3; i++) {
                int randomNumber = 0;
                boolean flag = true;
                while(flag) {
                    flag = false;
                    randomNumber = randomGenerator.nextInt(1,13);
                    for(int j=0; j<i; j++)
                        if (randomNumber == characters[j].getId()) {
                            flag = true;
                            break;
                        }
                }
                characters[i] = Character.generateCharacter(id, randomNumber, bag);
            }
        } else {
            characters = null;
        }

        this.professors = new Professor[5];
        for (int i=0; i<5; i++)
            professors[i] = new Professor(id, StudentColor.fromNumber(i));


        cloudList = new ArrayList<>();
        for(int i = 0; i<playerNumber.getWizardNumber(); i++)
            cloudList.add(new Cloud(i, bag, this.playerNumber));

        wizardList = new ArrayList<>();
        for (int i=0; i<playerNumber.getWizardNumber(); i++)
            wizardList.add(new Wizard(i, bag.requestStudents(playerNumber.getEntranceNumber()),
                    playerNumber == PlayerNumber.FOUR ? Tower.fromNumber(i%2) : Tower.fromNumber(i), playerNumber.getTowerNumber()));

        this.gameState = new PlanningState(id, wizardList.stream().map(Wizard::getId).collect(Collectors.toList()), randomGenerator);

        gameEnded = false;
        winner = null;

        List<StudentColor> flush = bag.takeRecentlySelected();
    }

    /**
     * Factory method to create a new game
     * @param gameMode easy or complete mode
     * @param playerNumber two, three or four players
     * @return Returns the id of the created match
     */
    public static Integer gameEntityFactory(GameMode gameMode, PlayerNumber playerNumber) {
        if (gameEntities == null) gameEntities = new ArrayList<>();
        Game generatedGame = new Game(idCount, gameMode, playerNumber);
        gameEntities.add(generatedGame);
        generatedGame.initializeGame();
        return idCount++;
    }

    /**
     * Method to serialize a game, and save it to the disk
     * @return string of the serialization
     */
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
    public static Integer deserializeGameFromFile (String fileName) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String in = reader.readLine();
        reader.close();

        return deserializeGameFromString(in);
    }

    public static Integer deserializeGameFromString(String string) {
        if (deserializationGson == null) initializeDeserializationGson();

        if (gameEntities == null) gameEntities = new ArrayList<>();

        Game newGame = JsonDeserializerClass.getGson().fromJson(string, Game.class);
        Game.gameEntities.add(newGame);
        newGame.id = idCount++;

        Arrays.stream(newGame.professors).forEach(x -> x.refreshGameId(newGame));

        if (newGame.characters != null) {
            for (Character c : newGame.characters) {
                c.refreshGameId(newGame);
                if (c.getId() == 1) ((CharacterOne) c).refreshBag(newGame.bag);
                else if (c.getId() == 11) ((CharacterEleven) c).refreshBag(newGame.bag);
            }
        }

        newGame.gameState.refreshGameId(newGame);

        newGame.cloudList.forEach(x -> x.setBag(newGame.bag));

        return newGame.id;
    }

    /**
     * Generate the objects to deserialize the game form json
     */
    private static void initializeDeserializationGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
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
        List <Game> result = gameEntities.stream().filter(x -> x.isGameId(gameId)).toList();
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
            if (islandGroupList.get(i).getTower() != null && islandGroupList.get(i).getTower() == islandGroupList.get((i+1)%islandGroupList.size()).getTower()) {
                islandGroupList.get((i+1)%islandGroupList.size()).getIslandList().addAll(0, islandGroupList.get(i).getIslandList());
                islandGroupList.remove(i);
                i--;
            }
        }
        if (islandGroupList.size() <= 3) endGame();
    }

    public void endGame() {
        gameEnded = true;
        calculateWinner();
    }

    /**
     * calculates the winner, and puts it in the winner attribute
     */
    private void calculateWinner() {
        List<Tower> winningTowers = new ArrayList<>();
        int minValue = 8;
        for (int i=0; i<playerNumber.getWizardNumber(); i++) {
            int current = wizardList.get(i).getTowerNumber();
            if (current < minValue)
                winningTowers = new ArrayList<>();
            if (current <= minValue) {
                winningTowers.add(Tower.fromNumber(i));
                minValue = current;
            }
        }
        if (winningTowers.size() == 1) {
            winner = winningTowers.get(0);
            return;
        }
        int[] professorNumber = new int[winningTowers.size()];
        List<Tower> finalWinners = new ArrayList<>();
        for (Professor p : professors) {
            Tower t = p.getMaster(null).getTowerColor();
            if (winningTowers.contains(t)) professorNumber[winningTowers.indexOf(t)]++;
        }
        int maxValue = -1;
        for (int i=0; i<winningTowers.size(); i++) {
            if (professorNumber[i] > maxValue)
                finalWinners = new ArrayList<>();
            if (professorNumber[i] >= maxValue) {
                finalWinners.add(winningTowers.get(i));
                maxValue = professorNumber[i];
            }
        }
        if (finalWinners.size() == 1) winner = finalWinners.get(0);
    }

    public void deleteGame() throws Exception {
        if (!gameEntities.contains(this)) throw new Exception("Error deleting the game");
        gameEntities.remove(this);
    }

    public Wizard getWizard(Integer wizardId) {
        for (Wizard w : wizardList)
            if (Objects.equals(w.getId(), wizardId)) return w;
        return null;
    }

    public List<Wizard> getWizardsFromTower(Tower towerColor) {
        return wizardList.stream().filter(w -> Objects.equals(w.getTowerColor(), towerColor)).collect(Collectors.toList());
    }

    public List<Wizard> getAllWizards() { return wizardList; }

    public Island getIsland(Integer islandId) {
        for(Island i : islandGroupList.stream().flatMap(x -> x.getIslandList().stream()).toList())
            if (Objects.equals(i.getId(), islandId)) return i;
        return null;
    }

    public Cloud getCloud(Integer cloudId) {
        for (Cloud c : cloudList)
            if (Objects.equals(c.getId(), cloudId))
                return c;
        return null;
    }

    public IslandGroup getIslandGroup(Integer islandGroupId) {
        for (IslandGroup g : islandGroupList)
            if (Objects.equals(g.getId(), islandGroupId))
                return g;
        return null;
    }

    public IslandGroup getFistIslandGroup () { return islandGroupList.get(0); }

    public Professor getProfessor(StudentColor studentColor) { return professors[studentColor.getValue()]; }

    public void updateGameState(GameState newState) { gameState = newState; }

    public PlayerNumber getPlayerNumber() { return playerNumber; }

    public List<IslandGroup> getIslandGroupList() { return islandGroupList; }

    public List<Cloud> getCloudList() { return cloudList; }

    private boolean isGameId(Integer id) { return id.equals(this.id); }

    public Integer getId () { return id; }

    public GameState getGameState() { return gameState; }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Character getCharacter(int characterId) throws Exception {
        for (Character c : characters)
            if (c.getId() == characterId) return c;
        throw new Exception("Character not found");
    }

    public Bag getBag() {
        return bag;
    }

    public boolean isGameEnded () {
        return gameEnded;
    }

    public Character[] getCharacters () {
        return characters;
    }

    public Tower getWinner () {
        return winner;
    }
}
