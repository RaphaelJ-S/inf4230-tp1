public class Colis {
    protected int uniqueIdentifier;
    protected Emplacement destination;

    public Colis(final int _pid){
        this.uniqueIdentifier = _pid;
    }

    @Override
    public int hashCode(){
        return this.uniqueIdentifier;
    }

    @Override
    public boolean equals(final Object _o){
        final Colis p = (Colis) _o;
        return this.uniqueIdentifier == p.uniqueIdentifier;
    }
}
