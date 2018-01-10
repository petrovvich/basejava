package com.petrovvich.webapp.storage;

import com.petrovvich.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    private static final int STORAGE_CAPACITY = 10000;

    protected Resume[] storage = new Resume[STORAGE_CAPACITY];

    protected int sizeOfArray = 0;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, sizeOfArray, null);
        sizeOfArray = 0;
    }

    @Override
    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index == -1) {
            System.out.println("Резюме не найдено");
        } else {
            storage[index] = resume;
        }
    }

    @Override
    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index >= 0) {
            System.out.println("Такое резюме уже есть в базе!");
        } else if (sizeOfArray == STORAGE_CAPACITY) {
            System.out.println("База резюме заполнена, удалите элементы, прежде чем вставлять новые!");
        } else {
            insertElement(r);
            sizeOfArray++;
        }
    }

    protected abstract void insertElement(Resume r);

    @Override
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("Такого резюме нет в базе");
            return null;
        }
        return storage[index];
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("Такого резюме нет в базе");
        } else {
            deleteElement(index);
            storage[sizeOfArray - 1] = null;
            sizeOfArray--;
        }
    }

    protected abstract void deleteElement(int index);

    protected abstract int getIndex(String uuid);

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, sizeOfArray);
    }

    @Override
    public int size() {
        return sizeOfArray;
    }
}