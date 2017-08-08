package io.github.jhipster.jpa2jdl.relationship;

public enum RelationshipType {
        OneToMany("OneToMany", false, true),
        OneToOne("OneToOne", false, false),
        ManyToMany("ManyToMany", true, true),
        ManyToOne("ManyToOne", true, false);

        private final String name;
        private final boolean fromMany;
        private final boolean toMany;

        RelationshipType(final String name, final boolean fromMany, final boolean toMany) {
            this.name = name;
            this.fromMany = fromMany;
            this.toMany = toMany;
        }

        public String getName() {
            return name;
        }

        public boolean isFromMany() {
            return fromMany;
        }

        public boolean isToMany() {
            return toMany;
        }
    }