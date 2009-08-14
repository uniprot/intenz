package uk.ac.ebi.intenz.domain.enzyme;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.View;

/**
 * A collection of <i>alternative</i> reactions catalyzed by one enzyme entry.
 * Each reaction can have a view set independently.
 * <br>
 * Note that sequential reactions (steps in an overall reaction) and coupled
 * reactions (elementary reactions in undefined order) should be handled in
 * the {@link #uk.ac.ebi.intenz.webapp.domain.Reaction Reaction} class.
 * @author rafalcan
 *
 */
public class EnzymaticReactions {

    /**
     * Set of reactions catalyzed (controlled) by the enzyme.
     */
    private Set<VisibleReaction> reactions;

    /**
     * Utility class to wrap a Reaction with the views where it is available.
     * @author rafalcan
     */
    private class VisibleReaction {
        private Reaction reaction;
        private EnzymeViewConstant view;
        private VisibleReaction(Reaction reaction, EnzymeViewConstant view){
            this.reaction = reaction;
            this.view = view;
        }
        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof VisibleReaction)) return false;
            return ((VisibleReaction) obj).reaction.equals(reaction);
        }
        @Override
		public int hashCode(){
			return reaction.hashCode();
		}
    }

    public EnzymaticReactions(){}

    /**
     * @return the number of reactions held by this object.
     */
    public int size(){
        return reactions.size();
    }

    public Reaction getReaction(int i){
        return ((VisibleReaction) reactions.toArray()[i]).reaction;
    }

    /**
     * @param view
     * @return a List containint the Reactions whose view match the parameter.
     *      For the IntEnz view, if there is at least one public Rhea reaction,
     *      only public Rhea reactions are returned.
     */
    public List<Reaction> getReactions(View theView){
    	boolean isInView, isRhea, isPublicRhea;
        List<Reaction> reactionsInView = new ArrayList<Reaction>();
        List<Reaction> rheaReactions = new ArrayList<Reaction>();
        for (Iterator<VisibleReaction> it = reactions.iterator(); it.hasNext();) {
            VisibleReaction wr = it.next();
            isRhea = wr.reaction.getId() > Reaction.NO_ID_ASSIGNED;
            isPublicRhea = isRhea
                    && wr.reaction.getStatus().isPublic()
                    && wr.reaction.isMapped();
            switch (theView) {
			case INTENZ:
				isInView = wr.view.isInIntEnzView();
                if (isInView){
                    if (!isRhea) {
                        reactionsInView.add(wr.reaction);
                    } else if (isPublicRhea){
                        rheaReactions.add(wr.reaction);
                    }
                }
				break;
			case IUBMB:
				isInView = wr.view.isInIUBMBView();
                if (isInView && !isRhea) reactionsInView.add(wr.reaction);
				break;
			case SIB:
				isInView = wr.view.isInSIBView();
                if (isInView && !isRhea) reactionsInView.add(wr.reaction);
				break;
			}
        }
        return rheaReactions.isEmpty()? reactionsInView : rheaReactions;
    }

    public EnzymeViewConstant getReactionView(int i){
        return ((VisibleReaction) reactions.toArray()[i]).view;
    }

    /**
     * Adds a reaction to the list. If it is already listed, even with a
     * different web view, nothing is done.
     * @param reaction
     * @param view
     * @return <code>true</code> if the set of reactions is changed.
     */
    public boolean add(Reaction reaction, String view){
        if (reactions == null) reactions = new LinkedHashSet<VisibleReaction>(4);
        return reactions.add(new VisibleReaction(reaction, EnzymeViewConstant.valueOf(view)));
    }

    /**
     * Adds several reactions to the list in a go.
     * @param er
     * @return <code>true</code> if the set of reactions is changed.
     */
    public boolean add(EnzymaticReactions er){
        if (reactions == null) reactions = new LinkedHashSet<VisibleReaction>(4);
    	return reactions.addAll(er.reactions);
    }

    /**
     * Removes a reaction from the list, no matter its view.
     * @param reaction
     */
    public void removeReaction(Reaction reaction){
        for (Iterator<VisibleReaction> it = reactions.iterator(); it.hasNext();) {
            VisibleReaction wr = it.next();
            if (wr.reaction.equals(reaction)){
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
        if (this == obj) return true;
        if (!(obj instanceof EnzymaticReactions)) return false;
        EnzymaticReactions er = (EnzymaticReactions) obj;
        if (er.reactions.size() != this.reactions.size()) return false;
        for (int i = 0; i < this.reactions.size(); i++) {
            if (!this.getReaction(i).equals(er.getReaction(i)))
                return false;
            if (!this.getReactionView(i).equals(er.getReactionView(i)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + (this.reactions != null ? this.reactions.hashCode() : 0);
        return hash;
    }

}
