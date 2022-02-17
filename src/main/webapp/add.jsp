<%@ page import="java.util.ArrayList" %>
<%@ page import="hocqu.Entities.Model" %>
<html>
    <head>
        <title>Add products</title>
    </head>
    <body>
            <div>
                <div>
                    <h2>Add products</h2>
                </div>
                <form  method="post" >
                    <label>Name:
                        <input type="text" name="name"><br />
                    </label>
                    <button type="submit">Submit</button>
                </form>
                <form  method="post">
                     <label>Delete id:
                         <input type="number" name="delete"><br />
                     </label>
                     <button type="submit">Delete</button>
                </form>
        </div>
        <div>
            <button onclick="location.href='/app/static'">Back to main</button>
        </div>
                <form  action="/app/add" method="post">
                    <button type="submit">View receipts</button>
                </form>
        <%
       String name=request.getParameter("name");
       String id=request.getParameter("delete");
        %>
  <%
  Model model = Model.getInstance();
  if(!(name==null) && !(name.equals("")) && !(model.find(name.toLowerCase()))){
  name=name.toLowerCase();
  model.add(name);}
  if(!(id==null) && !(id.equals(""))&& !(id.equals("0"))){
  Integer deleteId = Integer.valueOf(id)-1;
  if(deleteId<(model.size())){
  model.delete(deleteId);}}
            for(int i = 0; i < model.size(); i++){
                out.println("<br>"+(i+1)+"st: "+model.get(i));
            }
  pageContext.setAttribute("models", model, PageContext.APPLICATION_SCOPE);
        %>
    </body>
</html>