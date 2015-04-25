package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CommandClient {

    //返回菜单
    protected static final Object[] BACK = {"Q", "Back to previous menu", null};
    protected static final String UNDERLINE = "-----------------------------------------------------------------------------";
   
    //设置菜单
    private static final Object[][] SET_MENU = {
        // mnemonic, description, method
        {"1", "Load Default value", "loadDefault"},
        {"2", "Set Remote Host and Port(example,localhost:8191)", "setHost"},
        {"3", "Set Welcome message", "setMessage"},
        BACK
    };
    
    private static final Object[][] DUMP_MENU = {
        // mnemonic, description, method
        {"1", "Load Default value", "loadDefault"},
        {"2", "Set Remote Host and Port(example,localhost:8191)", "setHost"},
        {"3", "Set Welcome message", "setMessage"},
        BACK
    };
    //主菜单 
    private static final Object[][] MAIN_MENU = {
        // mnemonic, description, method
        {"1", "Set", SET_MENU},
        {"2", "Check Server Status", "checkServerStatus"},
        {"3", "Shutdown Server", "shutdownServer"},
        {"4", "dump", DUMP_MENU},
        {"Q", "Quit", "quit"}
    };
 
 	protected BufferedReader _stdin;
    protected FormattingPrintWriter _stdout;
    protected String title = null;

   
    public void setHost()
     throws IOException
    {
     System.out.println("please enter connection string:");
     BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
     System.out.println("The host you set is " + stdin.readLine());
    }
    public void shutdownServer()
    {
     System.out.println("server has been shut down");
    }
    public void checkServerStatus()
    {
     System.out.println("server is OK");
    }
    public CommandClient()
    {
    	_stdin = new BufferedReader(new InputStreamReader(System.in));
        _stdout = new FormattingPrintWriter(System.out, true);
        title = "Command Client";
        _stdout.println("\n--- "+title+"  ---");
    }
    protected void quit() {
        System.exit(0);
    }

	private Method findMethod(String name) {
        @SuppressWarnings("rawtypes")
		Class cl = getClass();
        Method method = null;
        while (method == null) {
            try {
                method = cl.getDeclaredMethod(name, null);
            } catch (NoSuchMethodException e) {
                System.out.println("no method define");
                cl = cl.getSuperclass();
                if (cl == null) {
                    e.printStackTrace();
                    System.exit(1);
                }
             /*
                try
             {
              return cl.getDeclaredMethod("done", null);
             }
                catch (NoSuchMethodException ex) {ex.printStackTrace();}
                */
            }
        }
        return method;
    }
    protected String getChoice(String choices)
     throws IOException {
        while (true) {
            _stdout.print("> ");
            _stdout.flush();
            String line = _stdin.readLine().trim();
            if (line.length() == 1) {
                int choice = Character.toUpperCase(line.charAt(0));
                int index = choices.indexOf(choice);
                if (index != -1)
                    return choices.substring(index, index + 1);
            }
            _stdout.println("\007*** Choice must be one of: " + choices);
        }
    }
    //选择菜单选项，动态调用某个方法
    public void doMenu(Object[][] menu, boolean main)
    {
     this.doMenu(this.title,menu,main);
    }
    public void doMenu(String pTitle,Object[][] menu, boolean main)
    {
        synchronized (System.in) {
            Map actions = new HashMap();
            StringBuffer sb = new StringBuffer(menu.length);
            for (int i = 0; i < menu.length; i++) {
                Object mnemonic = menu[i][0];
                sb.append(mnemonic);
                Object action = menu[i][2];
                if (action instanceof String)
                    action = findMethod((String)action);
                actions.put(mnemonic, action);
            }
            String choices = sb.toString();

            while (true) {/////////////////////////////////////////////////////////////
                try {
                    String mnemonic;

                    _stdout.clearTabs();
                    _stdout.println("\n---   " + pTitle + "   ---");
                    _stdout.println("\nEnter choice:");
                    for (int i = 0; i < menu.length; i++)
                        _stdout.println(menu[i][0] + ") " + menu[i][1]);

                    // Get the user's selection.
                    mnemonic = getChoice(choices);
                    //System.out.println("mnemonic="+mnemonic);
                    for (int i = 0; i < menu.length; i++) {
                        Object[] entry = menu[i];
                        if (entry[0].equals(mnemonic)) {
                            Object action = actions.get(mnemonic);
                            if (action == null) {
                                return;
                            } else if (action instanceof Method) {
                                //System.out.println("selected,will do");
                                // Cast required to suppress JDK1.5 varargs compiler warning.
                                ((Method)action).invoke(this, (Object[])null);
                            } else {
                                doMenu((String)entry[1], (Object[][])action,
                                    false);
                            }
                        }
                    }
                } catch (Exception e) {
                    Throwable t = e;
                    if (e instanceof InvocationTargetException)
                        t = ((InvocationTargetException)e).getTargetException();
                    _stdout.println("\007*** Caught exception: " + t);
                }
            }////////////////////////////////////////////////////////////////////////////
        }
    }
 /**
  * @param args
  */
 public static void main(String[] args)
 {
  new CommandClient().doMenu(MAIN_MENU,true);
 }
 public void setMessage()
  throws IOException
 {
  System.out.println("please enter welcome message:");
     BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
     System.out.println("welcome message  is " + stdin.readLine());
 }
 public void loadDefault()
 {
  System.out.println("default setting is loaded");
 }

}