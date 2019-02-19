/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbpmemail;

import bitronix.tm.TransactionManagerServices;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
//import org.drools.core.io.impl.ClassPathResource;
//import org.drools.core.marshalling.impl.ProtobufMessages.KnowledgeBase;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
//import org.drools.runtime.StatefulKnowledgeSession;
//import org.jbpm.process.workitem.bpmn2.ServiceTaskHandler;
import org.drools.KnowledgeBase;
import org.drools.core.io.impl.ClassPathResource;
import org.drools.io.Resource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItemHandler;
import org.jbpm.bpmn2.handler.ServiceTaskHandler;



/**
 *
 * @author faya
 */
public class test {
    static String persistenceUnitName = "PersistenceUnitTest";
    static String filePath="jbpmemail/ServiceTache.bpmn";
    static String processId="ServiceTache_PID";
        public void testServiceTask(EntityManagerFactory emf)throws Exception {
            KnowledgeBase kbase = createKnowledgeBase();
            Environment env = KnowledgeBaseFactory.newEnvironment();
            env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, emf);
            env.set(EnvironmentName.TRANSACTION_MANAGER, TransactionManagerServices.getTransactionManager());
            StatefulKnowledgeSession session = JPAKnowledgeService.newStatefulKnowledgeSession(kbase, null, env);
            session.getWorkItemManager().registerWorkItemHandler("Service Tache", (WorkItemHandler) new ServiceTaskHandler());
            KnowledgeRuntimeLoggerFactory.newConsoleLogger(session);
            
            
            Email email = new Email("Demo Subject", "Demo Body");
            Map<String,Object> params =new HashMap<String,Object>();
            params.put("email",email);
            
            session.startProcess(processId,params);
        }
    
    public static void main(String[] args) throws Exception{
        test call = new test();
        EntityManagerFactory emf=null;
        try{
        emf=Persistence.createEntityManagerFactory(persistenceUnitName);
        call.testServiceTask(emf);
        } catch(Exception e){
            Logger.getLogger(test.class.getName()).log(Level.SEVERE,null, e);
            }
        finally{
          emf.close();
        }
    }
    private KnowledgeBase createKnowledgeBase() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add((Resource) new ClassPathResource(filePath),ResourceType.BPMN2);
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        if(kbuilder.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            for (KnowledgeBuilderError error : kbuilder.getErrors() ){
                errorMessage.append(error.getMessage());
                errorMessage.append(System.getProperty("line.separator"));
            
            }
            System.out.println("errorMessage : " + errorMessage);
        }
    return kbase;
    }
}
