import syntaxtree.*;
import visitor.*;
import symboltable.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class TypeCheckVisitor extends GJDepthFirst<String,TypeInfo> {
    //
    // User-generated visitor methods below
    //

    /**
     * f0 -> MainClass()
     * f1 -> ( TypeDeclaration() )*
     * f2 -> <EOF>
     */
    public String visit(Goal n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */

    // To do: pass the main method to varDeclaration & Statement()
    // Typecheck them later
    public String visit(MainClass n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);

        SymbolTable st = SymbolTable.getInstance();
        String className = n.f1.f0.toString();
        ClassInfo classTable = st.lookUp(className);
        MethodsInfo mTable = classTable.getMethod("main");

        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        n.f13.accept(this, argu);
        n.f14.accept(this, mTable);
        n.f15.accept(this, mTable);
        n.f16.accept(this, argu);
        n.f17.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> ClassDeclaration()
     *       | ClassExtendsDeclaration()
     */
    public String visit(TypeDeclaration n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    public String visit(ClassDeclaration n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        // we check the repetition when add it to symboltable
        // no need to check again, just pass it down
        String className = n.f1.toString();
        SymbolTable st = SymbolTable.getInstance();
        ClassInfo classTable = st.lookUp(className);

        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, classTable);
        n.f4.accept(this, classTable);
        n.f5.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */

    // To do: check parent class exist or not
    //        check overloading
    //        check acylical
    public String visit(ClassExtendsDeclaration n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);

        String className = n.f1.toString();
        SymbolTable st = SymbolTable.getInstance();
        ClassInfo classTable = st.lookUp(className);

        if (classTable == null) {
            System.out.println("Class ERROR");
        }

        n.f2.accept(this, argu);
        String parentName = n.f2.toString();
        ClassInfo parentClassTable = st.lookUp(parentName);
        // parent exists or not
        if (parentClassTable == null){
            System.out.println("ClassExtendDeclaration ERROR");
            classTable.setParent(null);
        }
        // inherience cycle
        if (!st.acyclic(className, parentName)){
            System.out.println("Cylical Class ERROR");
        }
        // overloading
        if (st.isOverloading(className, parentName)){
            System.out.println("Class Overloading ERROR");
        }
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, classTable);
        n.f6.accept(this, classTable);
        n.f7.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    // repetition check done in building symbol table
    public String visit(VarDeclaration n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */
    // To do: check method type and return statement type
    public String visit(MethodDeclaration n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        String methodType = n.f1.accept(this, argu);
        String methodName = n.f2.accept(this, argu); // remember to modify the identifier()
        MethodsInfo mTable = ((ClassInfo)argu).getMethod(methodName);
        n.f3.accept(this, argu);
        n.f4.accept(this, mTable);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, mTable);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        String returnType = n.f10.accept(this, argu); //modify expression()
        if (!methodType.equals(returnType)){
            System.out.println("Mathod Return Type ERROR");
        }
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    public String visit(FormalParameterList n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    // parameter name checked in building symbol table
    public String visit(FormalParameter n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    public String visit(FormalParameterRest n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */
    // pass the type upward
    public String visit(Type n, TypeInfo argu) {
        String _ret=null;
        _ret = n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public String visit(ArrayType n, TypeInfo argu) {
        String _ret=null;
        _ret = "ARRAY";
//        n.f0.accept(this, argu);
//        n.f1.accept(this, argu);
//        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "boolean"
     */
    public String visit(BooleanType n, TypeInfo argu) {
        String _ret=null;
        _ret = "BOOLEAN";
        //n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "int"
     */
    public String visit(IntegerType n, TypeInfo argu) {
        String _ret=null;
        _ret = "INTEGER";
        //n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Block()
     *       | AssignmentStatement()
     *       | ArrayAssignmentStatement()
     *       | IfStatement()
     *       | WhileStatement()
     *       | PrintStatement()
     */
    public String visit(Statement n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "{"
     * f1 -> ( Statement() )*
     * f2 -> "}"
     */
    public String visit(Block n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    // To do: check whether the variable name declared or not
    //        check whether the type of left and right match
    public String visit(AssignmentStatement n, TypeInfo argu) {
        String _ret=null;
        String id = n.f0.accept(this, argu); //identifier() returns name
        // get the type of id
        String idType;
        n.f1.accept(this, argu);
        String exprType = n.f2.accept(this, argu);

        if (argu instanceof MethodsInfo){
            idType = ((MethodsInfo)argu).isIntialized(id);
            if (idType == null){
                System.out.println("Identifier not initialized");
            }else {
                if (!idType.equals(exprType)){
                    System.out.println("Assignment type not match");
            }
            }
        }

        n.f3.accept(this, argu);

        SymbolTable st = SymbolTable.getInstance();

        return _ret;
    }

    /**
     * f0 -> Identifier()
     * f1 -> "["
     * f2 -> Expression()
     * f3 -> "]"
     * f4 -> "="
     * f5 -> Expression()
     * f6 -> ";"
     */
    // To do: get identifier type
    //        [experssion] type must be int
    //        expression must be the same as identifier type
    public String visit(ArrayAssignmentStatement n, TypeInfo argu) {
        String _ret=null;
        String idType = n.f0.accept(this, argu);  //change identifier return value?
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "if"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     * f5 -> "else"
     * f6 -> Statement()
     */
    public String visit(IfStatement n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */
    public String visit(WhileStatement n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
    public String visit(PrintStatement n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> AndExpression()
     *       | CompareExpression()
     *       | PlusExpression()
     *       | MinusExpression()
     *       | TimesExpression()
     *       | ArrayLookup()
     *       | ArrayLength()
     *       | MessageSend()
     *       | PrimaryExpression()
     */
    public String visit(Expression n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    public String visit(AndExpression n, TypeInfo argu) {
        String _ret=null;
//        n.f0.accept(this, argu);
//        n.f1.accept(this, argu);
//        n.f2.accept(this, argu);
        _ret = "BOOLEAN";
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    public String visit(CompareExpression n, TypeInfo argu) {
        String _ret=null;
        _ret = "BOOLEAN";
//        n.f0.accept(this, argu);
//        n.f1.accept(this, argu);
//        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    public String visit(PlusExpression n, TypeInfo argu) {
        String _ret=null;
        _ret = "INTEGER";
//        n.f0.accept(this, argu);
//        n.f1.accept(this, argu);
//        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    public String visit(MinusExpression n, TypeInfo argu) {
        String _ret=null;
        _ret = "INTEGER";
//        n.f0.accept(this, argu);
//        n.f1.accept(this, argu);
//        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    public String visit(TimesExpression n, TypeInfo argu) {
        String _ret=null;
        _ret = "INTEGER";
//        n.f0.accept(this, argu);
//        n.f1.accept(this, argu);
//        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    public String visit(ArrayLookup n, TypeInfo argu) {
        String _ret=null;
        _ret = "ARRAY";
//        n.f0.accept(this, argu);
//        n.f1.accept(this, argu);
//        n.f2.accept(this, argu);
//        n.f3.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public String visit(ArrayLength n, TypeInfo argu) {
        String _ret=null;
        _ret = "INTEGER";
//        n.f0.accept(this, argu);
//        n.f1.accept(this, argu);
//        n.f2.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ExpressionList() )?
     * f5 -> ")"
     */
    public String visit(MessageSend n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public String visit(ExpressionList n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public String visit(ExpressionRest n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> IntegerLiteral()
     *       | TrueLiteral()
     *       | FalseLiteral()
     *       | Identifier()
     *       | ThisExpression()
     *       | ArrayAllocationExpression()
     *       | AllocationExpression()
     *       | NotExpression()
     *       | BracketExpression()
     */
    public String visit(PrimaryExpression n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public String visit(IntegerLiteral n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "true"
     */
    public String visit(TrueLiteral n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "false"
     */
    public String visit(FalseLiteral n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public String visit(Identifier n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "this"
     */
    public String visit(ThisExpression n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    public String visit(ArrayAllocationExpression n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public String visit(AllocationExpression n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public String visit(NotExpression n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public String visit(BracketExpression n, TypeInfo argu) {
        String _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

}
