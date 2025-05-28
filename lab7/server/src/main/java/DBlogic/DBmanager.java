package DBlogic;

import models.LabWork;

import java.util.Vector;

public interface DBmanager {
    Vector<LabWork> getCollectionFromDB();
    void  writeCollectionToDB();
}
