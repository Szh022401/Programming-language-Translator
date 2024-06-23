package provided;

/**
 * Interface for all Expression type tree nodes
 *
 */
public interface IExpression extends JottTree {

    /**
     * Will output a string of type of this Node
     * @return a string representing the type associated with this node
     */
    public String getType();

}
