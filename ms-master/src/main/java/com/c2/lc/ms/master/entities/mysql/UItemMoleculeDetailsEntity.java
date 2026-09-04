package com.c2.lc.ms.master.entities.mysql;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "u_item_molecule_details")
public class UItemMoleculeDetailsEntity {
    private String cItemCode;

    @Id
    @Column(name = "c_item_code", nullable = false, length = 50)
    public String getcItemCode() {
        return cItemCode;
    }

    public void setcItemCode(String cItemCode) {
        this.cItemCode = cItemCode;
    }

    private String cMoleculeCode;

    @Basic
    @Column(name = "c_molecule_code", nullable = true, length = 1000)
    public String getcMoleculeCode() {
        return cMoleculeCode;
    }

    public void setcMoleculeCode(String cMoleculeCode) {
        this.cMoleculeCode = cMoleculeCode;
    }

    private String cMoleculeName;

    @Basic
    @Column(name = "c_molecule_name", nullable = true, length = 1500)
    public String getcMoleculeName() {
        return cMoleculeName;
    }

    public void setcMoleculeName(String cMoleculeName) {
        this.cMoleculeName = cMoleculeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UItemMoleculeDetailsEntity that = (UItemMoleculeDetailsEntity) o;
        return Objects.equals(cItemCode, that.cItemCode) &&
                Objects.equals(cMoleculeCode, that.cMoleculeCode) &&
                Objects.equals(cMoleculeName, that.cMoleculeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cItemCode, cMoleculeCode, cMoleculeName);
    }
}
