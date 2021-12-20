package Tests;

import org.junit.Test;
import model.Slot;
import static org.junit.Assert.*;

public class SlotTest {

    @Test
    public void getSpace() {
        Slot s = new Slot(500,0);
        assertEquals(500,s.getSpace());
    }
}