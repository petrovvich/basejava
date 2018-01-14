package com.petrovvich.webapp.storage;

import com.petrovvich.webapp.exception.ExistStorageException;
import com.petrovvich.webapp.exception.NotExistStorageException;
import com.petrovvich.webapp.exception.StorageException;
import com.petrovvich.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public abstract class AbstractArrayStorageTest {

    private Storage storage = new ArrayStorage();

    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }
    private static final String UUID_1 = "uuid1";
    private static final Resume resume1 = new Resume(UUID_1);
    private static final String UUID_2 = "uuid2";
    private static final Resume resume2 = new Resume(UUID_2);
    private static final String UUID_3 = "uuid3";
    private static final Resume resume3 = new Resume(UUID_3);

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void update() throws Exception {
        assertSize(3);
        Resume resume = new Resume(UUID_1);
        storage.update(resume);
        assertSize(3);
        Assert.assertSame(storage.get(UUID_1), resume);
    }

    @Test
    public void save() throws Exception {
        assertSize(3);
        Resume testResume = new Resume("123456");
        storage.save(testResume);
        assertSize(4);
        Assert.assertSame(testResume, storage.get("123456"));
    }

    @Test
    public void get() throws Exception {
        String find = "uuid1";
        Assert.assertSame(find, storage.get("uuid1").toString());
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistResume() throws Exception {
        storage.get("putin");
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistResume() throws Exception {
        storage.save(resume1);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        assertSize(3);
        storage.delete("uuid1");
        assertSize(2);
        storage.get("uuid1");
    }

    @Test(expected = StorageException.class)
    public void storageOverloaded() {
        try {
            for (int i = 4; i <= AbstractArrayStorage.STORAGE_CAPACITY; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assert.fail();
        }
        storage.save(new Resume());
    }

    @Test
    public void getAll() throws Exception {
        Resume[] result = {new Resume(UUID_1), new Resume(UUID_2), new Resume(UUID_3)};
        Assert.assertArrayEquals(result, storage.getAll());
    }

    @Test
    public void size() throws Exception {
        assertSize(3);
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }
}