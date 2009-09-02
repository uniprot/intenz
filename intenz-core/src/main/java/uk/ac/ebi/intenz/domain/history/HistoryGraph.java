package uk.ac.ebi.intenz.domain.history;

import uk.ac.ebi.intenz.domain.constants.EventConstant;

import java.util.*;

/**
 * The directed acyclic history graph stores all events related to an enzyme.
 * <p/>
 * The list of all events may include all previous and following events, depending on the state of the regarded enzyme.
 * This implementation provides standard graph methods such as removing and inserting nodes and events. Each application
 * of these methods includes a check for cycles.
 * <p/>
 * See {@link HistoryEvent} and {@link HistoryNode} for more information about the elements
 * of the history graph.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:04 $
 */
public class HistoryGraph {
   /**
    * The enzyme node for which the history graph is created.
    */
   private HistoryNode rootNode;

   /**
    * Returns a <code>HistoryGraph</code> instance.
    *
    * @param rootNode The root node of the history graph.
    * @throws NullPointerException if <code>rootNode</code> is empty.
    */
   public HistoryGraph (HistoryNode rootNode) {
      if ( rootNode == null ) throw new NullPointerException("Parameter 'rootNode' must not be null.");
      this.rootNode = rootNode;
   }

   /**
    * Returns <code>true</code> if this node's event is a <code>DELETION</code> event.
    * <p/>
    * This is only the case if the root node is the <code>beforeNode</code> of the event. Otherwise the event is a
    * <code>CREATION</code> event where the root node represents the <code>afterNode</code>, that is the new entry
    * an enzyme has been deleted to.
    *
    * @return <code>true</code> if the root node's event is of type
    *         {@link uk.ac.ebi.intenz.domain.constants.EventConstant.DELETION}.
    */
   public boolean isDeletedRootNode () {
      return this.getLatestHistoryEventOfRoot().getEventClass().equals(EventConstant.DELETION)&&
            this.getLatestHistoryEventOfRoot().getBeforeNode() != null &&
            this.getLatestHistoryEventOfRoot().getBeforeNode().equals(rootNode);
   }

   /**
    * Returns <code>true</code> if this node's event is a <code>TRANSFER</code> event.
    * <p/>
    * This is only the case if the root node is the <code>beforeNode</code> of the event. Otherwise the event is a
    * <code>CREATION</code> event where the root node represents the <code>afterNode</code>, that is the new entry
    * an enzyme has been transferred to.
    *
    * @return <code>true</code> if the root node's event is of type
    *         {@link uk.ac.ebi.intenz.domain.constants.EventConstant.TRANSFER}.
    */
   public boolean isTransferredRootNode () {
      return this.getLatestHistoryEventOfRoot().getEventClass().equals(EventConstant.TRANSFER) &&
            this.getLatestHistoryEventOfRoot().getBeforeNode() != null &&
            this.getLatestHistoryEventOfRoot().getBeforeNode().equals(rootNode);
   }



   // ------------------- GETTER -----------------------------

   public HistoryNode getRootNode () {
      return rootNode;
   }

   public SortedSet<HistoryEvent> getEdges () {
      return retrieveEdges(rootNode, new HashSet<HistoryNode>());
   }

   /**
    * Returns the most recent event of the WHOLE graph.
    *
    * @return the most recent event.
    */
   public HistoryEvent getLatestHistoryEventOfAll () {
      SortedSet<HistoryEvent> edges = retrieveEdges(rootNode, new HashSet<HistoryNode>());
      if ( edges.size() == 0 )
         return new HistoryEvent();
	return edges.last();
   }

   /**
    * Returns the most recent event of the root node only.
    *
    * @return the most recent event.
    */
   public HistoryEvent getLatestHistoryEventOfRoot () {
      SortedSet<HistoryEvent> edges = new TreeSet<HistoryEvent>(rootNode.getEdges());
      if ( edges.size() == 0 )
         return new HistoryEvent();
	return edges.last();
   }

   /**
    * Returns the most recent event of the root node only.
    *
    * @return the most recent event.
    */
   public HistoryEvent getLatestRelevantHistoryEventOfRoot () {
	   SortedSet<HistoryEvent> edges = new TreeSet<HistoryEvent>(rootNode.getEdges());
	   if ( edges.size() == 0 )
		   return new HistoryEvent();
	   List<HistoryEvent> list = new ArrayList<HistoryEvent>(edges);
	   // count in reverse
	   for ( int iii = list.size()-1; iii > -1; iii-- ) {
		   HistoryEvent event = list.get(iii);
		   // Check if its a transferred node...
		   if ( event.getEventClass().equals(EventConstant.TRANSFER) ){
			   if ( isTransferredRootNode(event) ) {
				   return event;
			   }
			   continue;
		   }

		   // Only nodes which have a before_id same as our rootNode are actually modified
//		   if( event.getEventClass().equals(EventConstant.MODIFICATION) )
//		   if ( isModifiedRootNode(event) )
//		   return event;
//		   else
//		   continue;
		   return event;
	   }
	   return new HistoryEvent();
   }

   // ------------------- PRIVATE METHODS --------------------

   /**
    * Gets all events this node is part of.
    *
    * @param node The node to be looked at.
    * @return the sorted set of edges (history events).
    */
   private SortedSet<HistoryEvent> retrieveEdges (HistoryNode node, Set<HistoryNode> visitedEdges) {
      assert node != null;
      visitedEdges.add(node);
      SortedSet<HistoryEvent> edges = new TreeSet<HistoryEvent>();

      List<HistoryEvent> currentEdges = node.getEdges();
      for (Iterator<HistoryEvent> it = currentEdges.iterator(); it.hasNext();) {
         HistoryEvent event = it.next();
         HistoryNode relative = event.getRelative(node);

         if ( relative != null ) {
            if ( visitedEdges.contains(relative) ) return edges;
            edges.addAll(retrieveEdges(relative, visitedEdges));
         }

         edges.add(event);
      }

      return edges;
   }

   /**
    * Returns <code>true</code> if this the event is a <code>TRANSFER</code> event.
    * <p/>
    * This is only the case if the root node is the <code>beforeNode</code> of the event. Otherwise the event is a
    * <code>CREATION</code> event where the root node represents the <code>afterNode</code>, that is the new entry
    * an enzyme has been transferred to.
    *
    * @param event to verify if its a transferred enzyme
    * @return <code>true</code> if the root node's event is of type
    *         {@link uk.ac.ebi.intenz.domain.constants.EventConstant#TRANSFER}.
    */
   private boolean isTransferredRootNode (HistoryEvent event) {
      return event.getEventClass().equals(EventConstant.TRANSFER) &&
            event.getBeforeNode() != null &&
            event.getBeforeNode().equals(rootNode);
   }

   private boolean isModifiedRootNode (HistoryEvent event) {
      return event.getEventClass().equals(EventConstant.MODIFICATION) &&
            event.getBeforeNode() != null &&
            event.getAfterNode().equals(rootNode);
   }

   static class HistoryGraphGrid {

      private TreeMap<List<Integer>, Object> elements;
      private TreeSet<Integer> xCoordinates;
      private TreeSet<Integer> yCoordinates;

      public HistoryGraphGrid () {
    	  elements = new TreeMap<List<Integer>, Object>(new Comparator<List<Integer>>() {
    		  public int compare (List<Integer> al1, List<Integer> al2) {
    			  int x1 = al1.get(0).intValue();
    			  int y1 = al1.get(1).intValue();

    			  int x2 = al2.get(0).intValue();
    			  int y2 = al2.get(1).intValue();

    			  if ( x1 < x2 ) return -1;
    			  if ( x1 > x2 ) return 1;
    			  if ( y1 < y2 ) return -1;
    			  if ( y1 > y2 ) return 1;

    			  return 0;
    		  }
    	  });
    	  xCoordinates = new TreeSet<Integer>();
    	  yCoordinates = new TreeSet<Integer>();
      }

      public TreeMap<List<Integer>, Object> getElements () {
         return elements;
      }

      public boolean contains (int x, int y) {
         List<Integer> coordinates = new ArrayList<Integer>();
         coordinates.add(new Integer(x));
         coordinates.add(new Integer(y));
         return elements.containsKey(coordinates);
      }

      public void put (int x, int y, Object value) {
         List<Integer> coordinates = new ArrayList<Integer>();
         coordinates.add(new Integer(x));
         coordinates.add(new Integer(y));
         elements.put(coordinates, value);
         xCoordinates.add(new Integer(x));
         yCoordinates.add(new Integer(y));
      }

      public Object getValue (int x, int y) {
         List<Integer> coordinates = new ArrayList<Integer>();
         coordinates.add(new Integer(x));
         coordinates.add(new Integer(y));
         return elements.get(coordinates);
      }

      public void remove (int x, int y) {
         List<Integer> coordinates = new ArrayList<Integer>();
         coordinates.add(new Integer(x));
         coordinates.add(new Integer(y));
         elements.remove(coordinates);
         xCoordinates.remove(new Integer(x));
         yCoordinates.remove(new Integer(y));
      }

      public int getMinX () {
         return xCoordinates.first().intValue();
      }

      public int getMaxX () {
         return xCoordinates.last().intValue();
      }

      public int getMinY () {
         return yCoordinates.first().intValue();
      }

      public int getMaxY () {
         return yCoordinates.last().intValue();
      }

   }

}
