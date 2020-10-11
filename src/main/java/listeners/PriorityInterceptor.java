package listeners;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import java.util.ArrayList;
import java.util.List;

public class PriorityInterceptor implements IMethodInterceptor {

    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        List<IMethodInstance> arrangedMethodList = new ArrayList<>();

        for (IMethodInstance iMethodInstance : methods) {
            if (iMethodInstance.getMethod().getMethodName().toLowerCase().contains("logout"))
                arrangedMethodList.add(iMethodInstance);

        }

            for (IMethodInstance iMethodInstance : methods) {
            if (!iMethodInstance.getMethod().getMethodName().toLowerCase().contains("logout"))
                arrangedMethodList.add(iMethodInstance);

        }

        return arrangedMethodList;
    }
}
