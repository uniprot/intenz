package uk.ac.ebi.intenz.domain.enzyme;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.View;
import uk.ac.ebi.intenz.domain.enzyme.EnzymaticReactions.VisibleReaction;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.domain.Status;

/**
 * A collection of <i>alternative</i> reactions catalysed by one enzyme entry.
 * Each reaction can have a view set independently, and a IUBMB flag (main
 * reaction assigned by the organization to an EC number). Note that only Rhea
 * reactions can have this flag set (i.e. textual descriptions will never have
 * it).
 * <br>
 * Note that sequential reactions (steps in an overall reaction) and coupled
 * reactions (elementary reactions in undefined order) should be handled in
 * Rhea.
 *
 * @author rafalcan
 *
 */
public class EnzymaticReactions implements Collection<VisibleReaction> {

    /**
     * Set of reactions catalysed (controlled) by the enzyme.
     */
    private Set<VisibleReaction> reactions;

    /**
     * Utility class to wrap a Reaction with the views where it is available and
     * the IUBMB flag.
     *
     * @author rafalcan
     */
    public class VisibleReaction {

        private Reaction reaction;
        private EnzymeViewConstant view;
        private boolean iubmb;

        private VisibleReaction(Reaction reaction, EnzymeViewConstant view,
                boolean iubmb) {
            this.reaction = reaction;
            this.view = view;
            this.iubmb = iubmb;
        }

        public Reaction getReaction() {
            return reaction;
        }

        public EnzymeViewConstant getView() {
            return view;
        }

        public boolean isIubmb() {
            return iubmb;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof VisibleReaction)) {
                return false;
            }
            return ((VisibleReaction) obj).reaction.equals(reaction);
        }

        @Override
        public int hashCode() {
            if (reaction == null) {

                return Integer.MIN_VALUE;
            }
            return reaction.hashCode();
        }
    }

    public EnzymaticReactions() {
        reactions = new LinkedHashSet<VisibleReaction>(4);
    }

    /**
     * @return the number of reactions held by this object.
     */
    public int size() {
        return reactions.size();
    }

    public Reaction getReaction(int i) {
        return ((VisibleReaction) reactions.toArray()[i]).reaction;
    }

    /**
     * @param view
     * @return a List containing the Reactions whose view match the parameter.
     * For the IntEnz view, if there is at least one public Rhea reaction, only
     * public Rhea reactions are returned. Obsolete Rhea reactions are ignored.
     */
    public List<Reaction> getReactions(View theView) {
        List<Reaction> reactionsInView = new ArrayList<Reaction>();
        for (VisibleReaction vr : forView(theView)) {
            reactionsInView.add(vr.reaction);
        }
        return reactionsInView;
    }

    /**
     * Filters this collection of reactions for a given view.
     * @param view the view used as filter.
     * @return a subset of this collection, containing only reactions in the
     * 		given view. For the IntEnz view, if there is at least one public
     * 		Rhea reaction, only public Rhea reactions are returned.
     * @since 4.3.0
     */
    public EnzymaticReactions forView(View theView){
    	boolean isInView, isRhea, isPublicRhea;
        EnzymaticReactions reactionsInView = new EnzymaticReactions();
        EnzymaticReactions rheaReactions = new EnzymaticReactions();
        for (Iterator<VisibleReaction> it = reactions.iterator(); it.hasNext();) {
            VisibleReaction wr = it.next();
                isRhea = wr.reaction.getId() > Reaction.NO_ID_ASSIGNED;
            isPublicRhea = isRhea
                    && wr.reaction.getStatus().isPublic()
                    && !wr.reaction.getStatus().equals(Status.OB) // ignore OBs
                    && wr.reaction.isMapped();
            switch (theView) {
                case INTENZ:
                    isInView = wr.view.isInIntEnzView();
                    if (isInView) {
                        if (!isRhea) {
                            reactionsInView.add(wr);
                        } else if (isPublicRhea) {
                            rheaReactions.add(wr);
                        }
                    }
                    break;
                case IUBMB:
                    isInView = wr.view.isInIUBMBView();
                    if (isInView && !isRhea) {
                        reactionsInView.add(wr);
                    }
                    break;
                case SIB:
                    isInView = wr.view.isInSIBView();
                    if (isInView && !isRhea) {
                        reactionsInView.add(wr);
                    }
                    break;
            }
        }
        return rheaReactions.isEmpty() ? reactionsInView : rheaReactions;
    }

    public EnzymeViewConstant getReactionView(int i) {
        return ((VisibleReaction) reactions.toArray()[i]).view;
    }

    /**
     * Has a reaction the IUBMB flag set?
     *
     * @since 4.2.7
     * @param i the index of the reaction.
     * @return the IUBMB flag.
     */
    public boolean getReactionIubmbFlag(int i) {
        return ((VisibleReaction) reactions.toArray()[i]).iubmb;
    }

    /**
     * Adds a reaction to the list. If it is already listed, even with a
     * different web view, nothing is done.
     *
     * @param reaction
     * @param view
     * @param iubmb
     * @return <code>true</code> if the set of reactions is changed.
     */
    public boolean add(Reaction reaction, String view, boolean iubmb) {
        return reactions.add(new VisibleReaction(
                reaction, EnzymeViewConstant.valueOf(view), iubmb));
    }

    /**
     * Adds several reactions to the list in one go.
     *
     * @param er
     * @return <code>true</code> if the set of reactions is changed.
     */
    public boolean add(EnzymaticReactions er) {
        boolean changed = false;
        if (er != null && er.size() > 0) {
            if (reactions == null) {
                reactions = new LinkedHashSet<VisibleReaction>(4);
            }
            changed = reactions.addAll(er.reactions);
        }
        return changed;
    }

    /**
     * Removes a reaction from the list, no matter its view.
     *
     * @param reaction
     */
    public void removeReaction(Reaction reaction) {
        for (Iterator<VisibleReaction> it = reactions.iterator(); it.hasNext();) {
            VisibleReaction wr = it.next();
            if (wr.reaction.equals(reaction)) {
                it.remove();
                break;
            }
        }
    }

    public void setReactions(LinkedHashSet<VisibleReaction> reactions) {
        this.reactions = reactions;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EnzymaticReactions)) {
            return false;
        }
        EnzymaticReactions er = (EnzymaticReactions) obj;
        if (er.reactions.size() != this.reactions.size()) {
            return false;
        }
        for (int i = 0; i < this.reactions.size(); i++) {
            if (!this.getReaction(i).equals(er.getReaction(i))) {
                return false;
            }
            if (!this.getReactionView(i).equals(er.getReactionView(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + (this.reactions != null ? this.reactions.hashCode() : 0);
        return hash;
    }

    public Iterator<VisibleReaction> iterator() {
        return reactions.iterator();
    }

    public boolean add(VisibleReaction arg0) {
        return reactions.add(arg0);
    }

    public boolean addAll(Collection<? extends VisibleReaction> arg0) {
        return reactions.addAll(arg0);
    }

    public void clear() {
        reactions.clear();
    }

    public boolean contains(Object arg0) {
        return reactions.contains(arg0);
    }

    public boolean containsAll(Collection<?> arg0) {
        return reactions.containsAll(arg0);
    }

    public boolean isEmpty() {
        return reactions.isEmpty();
    }

    public boolean remove(Object arg0) {
        return reactions.remove(arg0);
    }

    public boolean removeAll(Collection<?> arg0) {
        return reactions.removeAll(arg0);
    }

    public boolean retainAll(Collection<?> arg0) {
        return reactions.retainAll(arg0);
    }

    public Object[] toArray() {
        return reactions.toArray();
    }

    public <VisibleReaction> VisibleReaction[] toArray(VisibleReaction[] arg0) {
        return null;
    }

}
