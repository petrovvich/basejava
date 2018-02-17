package com.petrovvich.webapp.storage;

import com.petrovvich.webapp.exception.ExistStorageException;
import com.petrovvich.webapp.exception.NotExistStorageException;
import com.petrovvich.webapp.exception.StorageException;
import com.petrovvich.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<SK> implements Storage {

    protected static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getUuid).thenComparing(Resume::getFullname);

    @Override
    public Resume get(String uuid) {
        SK searchIndex = getSearchIndex(uuid);
        boolean validateIndex = checkIndex(searchIndex);
        if (validateIndex) {
            throw new NotExistStorageException(uuid);
        }
        return getResumeFromStorage(searchIndex);
    }

    protected abstract Resume getResumeFromStorage(SK searchIndex);

    protected abstract boolean checkIndex(SK searchIndex);

    protected abstract SK getSearchIndex(String uuid);

    @Override
    public void delete(String uuid) {
        SK searchIndex = getSearchIndex(uuid);
        boolean validateIndex = checkIndex(searchIndex);
        if (validateIndex) {
            throw new NotExistStorageException(uuid);
        } else {
            deleteResumeFromStorage(searchIndex);
        }
    }

    protected abstract void deleteResumeFromStorage(SK searchIndex);

    @Override
    public void save(Resume r) {
        SK searchIndex = getSearchIndex(r.getUuid());
        boolean validateIndex = checkIndex(searchIndex);
        if (!validateIndex) {
            throw new ExistStorageException(r.getUuid());
        } else {
            insertElementInStorage(searchIndex, r);
        }
    }

    protected abstract void insertElementInStorage(SK searchIndex, Resume resume);

    public void update(Resume resume) {
        SK searchIndex = getSearchIndex(resume.getUuid());
        boolean validateIndex = checkIndex(searchIndex);
        if (validateIndex) {
            throw new ExistStorageException(resume.getUuid());
        }
        updateElementInStorage(searchIndex, resume);
    }

    protected abstract void updateElementInStorage(SK searchIndex, Resume resume);

    public List<Resume> getAllSorted() {
        List<Resume> result = getListedResumes();
        result.sort(RESUME_COMPARATOR);
        return result;
    }

    protected abstract List<Resume> getListedResumes();

    public abstract int size();
}
