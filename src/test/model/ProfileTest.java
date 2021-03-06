package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import persistence.JsonTest;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileTest {
    private Profile user;
    private Profile bag;
    private Food food;
    private Food food1;

    @BeforeEach
    public void runBefore() {
        food = new Food(FoodType.COOKIE, 5);
        bag = new Profile("test", 10, "Rufus", PetType.DOG);
    }

    @Test
    public void makeUserZero() {
        user = new Profile("Joe", -1, "test", PetType.DOG);
        assertEquals(0, user.getBalance());
    }

    @Test
    public void testGetName() {
        assertEquals("test", bag.getName());
        assertEquals("Rufus", bag.getPetName());
    }

    @Test
    public void testGetSlots() {
        List<Slot> storage = bag.getSlots();
        assertEquals(4, storage.size());
        assertTrue(bag.isEmpty());
    }

    @Test
    public void testFindFood() {
        assertTrue(bag.isEmpty());
        assertEquals(-1, bag.findFood(food));
        bag.addFood(food, 1);
        assertEquals(0, bag.findFood(food));
    }

    @Test
    public void testGetType() {
        assertEquals(PetType.DOG, bag.getPetType());
        user = new Profile("Joe", 5, "test", PetType.CAT);
        assertEquals(PetType.CAT, user.getPetType());
    }

    @Test
    public void testGetNameNumbers() {
        user = new Profile("25th Bob", 5, "test", PetType.DOG);
        assertEquals("25th Bob", user.getName());
    }

    @Test
    public void testGetBalance() {
        user = new Profile("Joe", 5, "test", PetType.DOG);
        assertEquals(5, user.getBalance());
    }

    @Test
    public void testGetBalanceZero() {
        user = new Profile("Broke Joe", 0, "test", PetType.DOG);
        assertEquals(0, user.getBalance());
    }

    @Test
    public void testGetBalanceLot() {
        user = new Profile("Stacked Joe", 10000, "test", PetType.DOG);
        assertEquals(10000, user.getBalance());
    }

    @Test
    public void testAddBal() {
        user = new Profile("Joe", 5, "test", PetType.DOG);
        user.addBalance(10);
        assertEquals(15, user.getBalance());
    }

    @Test
    public void testAddBalNone() {
        user = new Profile("Joe", 5, "test", PetType.DOG);
        user.addBalance(0);
        assertEquals(5, user.getBalance());
    }

    @Test
    public void testSubBal() {
        user = new Profile("Joe", 10, "test", PetType.DOG);
        user.subBalance(5);
        assertEquals(5, user.getBalance());
    }

    @Test
    public void testSubBalNone() {
        user = new Profile("Joe", 10, "test", PetType.DOG);
        user.subBalance(0);
        assertEquals(10, user.getBalance());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(bag.isEmpty());
    }

    @Test
    public void testReadSlot() {
        bag.addFood(food, 1);
        assertEquals(food, bag.readSlot(0).getFood());
        assertEquals(1, bag.readSlot(0).getQuantity());

        assertEquals(0, bag.readSlot(1).getQuantity());
    }

    @Test
    public void testReadSlotMultiple() {
        food1 = new Food(FoodType.ICE_CREAM, 2);
        bag.addFood(food, 2);
        bag.addFood(food1, 1);

        assertEquals(food, bag.readSlot(0).getFood());
        assertEquals(2, bag.readSlot(0).getQuantity());
        assertEquals(food1, bag.readSlot(1).getFood());
        assertEquals(1, bag.readSlot(1).getQuantity());
    }

    @Test
    public void testAddEmpty() {
        assertTrue(bag.isEmpty());

        boolean bool = bag.addFood(food, 1);
        assertTrue(bool);
        assertEquals(food, bag.readSlot(0).getFood());
        assertEquals(1, bag.readSlot(0).getQuantity());
        assertFalse(bag.isEmpty());
    }

    @Test
    public void testAddNotFull() {
        food1 = new Food(FoodType.ICE_CREAM, 1);
        bag.addFood(food, 1);
        assertFalse(bag.isEmpty());

        boolean bool1 = bag.addFood(food1, 1);
        assertTrue(bool1);
        assertEquals(food1, bag.readSlot(1).getFood());
        assertEquals(1, bag.readSlot(1).getQuantity());
    }

    @Test
    public void testAddFull() {
        for (int i = 0; i < Profile.MAX_SIZE; i++) {
            bag.addFood(new Food(FoodType.COOKIE, 1 + i), 1);
        }

        food1 = new Food(FoodType.ICE_CREAM, 1);
        boolean bool1 = bag.addFood(food1, 1);
        assertFalse(bool1);
    }

    @Test
    public void testAddSame() {
        for (int i = 0; i < Profile.MAX_SIZE; i++) {
            bag.addFood(food, 1);
        }

        food1 = new Food(FoodType.ICE_CREAM, 1);
        boolean bool1 = bag.addFood(food1, 1);
        assertTrue(bool1);
    }

    @Test
    public void testRemoveEmpty() {
        assertTrue(bag.isEmpty());
        assertFalse(bag.removeFood(food, 1));
    }

    @Test
    public void testRemoveClearSlot() {
        bag.addFood(food, 1);
        assertFalse(bag.isEmpty());

        assertTrue(bag.removeFood(food, 1));
        assertTrue(bag.isEmpty());
    }

    @Test
    public void testRemoveSubAmount() {
        bag.addFood(food, 2);
        assertFalse(bag.isEmpty());

        assertTrue(bag.removeFood(food, 1));
        assertFalse(bag.isEmpty());
        assertTrue(bag.removeFood(food, 1));
        assertTrue(bag.isEmpty());
    }

    @Test
    public void testSetSlot() {
        bag.addFood(food, 2);
        assertEquals(2, bag.readSlot(0).getQuantity());

        Slot slot = new Slot(Food.ICE_CREAM, 1);
        bag.setSlot(slot, 0);
        assertEquals(1, bag.readSlot(0).getQuantity());

    }
}
