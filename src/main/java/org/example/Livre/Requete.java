package org.example.Livre;

import java.io.Serializable;

public class Requete implements Serializable {
    private static final long serialVersionUID = 1L;

    private TypeRequete type;

    public Requete(TypeRequete type) {
        this.type = type;
    }

    public TypeRequete getType() {
        return type;
    }

    public void setType(TypeRequete type) {
        this.type = type;
    }
}
