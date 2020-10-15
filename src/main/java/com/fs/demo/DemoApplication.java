package com.fs.demo;

import com.documents4j.api.DocumentType;
import com.fs.demo.domain.Repayment;
import org.docx4j.documents4j.local.Documents4jLocalServices;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import pl.jsolve.templ4docx.variable.TableVariable;
import pl.jsolve.templ4docx.variable.TextVariable;
import pl.jsolve.templ4docx.variable.Variable;
import pl.jsolve.templ4docx.variable.Variables;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var docx = new Docx("template.docx");
        docx.setVariablePattern(new VariablePattern("#{", "}"));

        // preparing variables
        var variables = new Variables();
        variables.addTextVariable(new TextVariable("#{id}", "23434323888"));
        variables.addTextVariable(new TextVariable("#{t_o}", "300"));
        variables.addTextVariable(new TextVariable("#{t_p}", "300"));
        variables.addTextVariable(new TextVariable("#{t_i}", "30"));
        variables.addTextVariable(new TextVariable("#{t_t}", "330"));

        // table
        var tableVariable = new TableVariable();

        var snList = new ArrayList<Variable>();
        var dayList = new ArrayList<Variable>();
        var dateList = new ArrayList<Variable>();
        var outstandingList = new ArrayList<Variable>();
        var principleList = new ArrayList<Variable>();
        var interestList = new ArrayList<Variable>();
        var totalList = new ArrayList<Variable>();

        var repayments = initData();
        repayments.forEach(repayment -> {
            snList.add(new TextVariable("#{sn}", repayment.getSn()));
            dayList.add(new TextVariable("#{day}", repayment.getDay()));
            dateList.add(new TextVariable("#{date}", repayment.getDate()));
            outstandingList.add(new TextVariable("#{principle}", repayment.getPrinciple()));
            principleList.add(new TextVariable("#{outstanding}", repayment.getOutstanding()));
            interestList.add(new TextVariable("#{interest}", repayment.getInterest()));
            totalList.add(new TextVariable("#{total}", repayment.getTotal()));
        });

        tableVariable.addVariable(snList);
        tableVariable.addVariable(dateList);
        tableVariable.addVariable(dayList);
        tableVariable.addVariable(outstandingList);
        tableVariable.addVariable(principleList);
        tableVariable.addVariable(interestList);
        tableVariable.addVariable(totalList);

        variables.addTableVariable(tableVariable);

        // fill template
        docx.fillTemplate(variables);

        // save filled .docx file
        docx.save("result/result.docx");

        var inFile = new File(System.getProperty("user.dir") + "/result/result.docx");
        var outFile = new File(System.getProperty("user.dir") + "/result/result.pdf");
        FileOutputStream fos = new FileOutputStream(outFile);

        Documents4jLocalServices exporter = new Documents4jLocalServices();
        exporter.export(inFile, fos, DocumentType.MS_WORD);
        fos.close();
    }

    private List<Repayment> initData() {
        var repay1 = new Repayment()
                .setSn("1")
                .setDay("Monday")
                .setDate(LocalDate.now().toString())
                .setInterest("3")
                .setPrinciple("10")
                .setOutstanding("100")
                .setTotal("13");
        var repay2 = new Repayment()
                .setSn("2")
                .setDay("Monday")
                .setDate(LocalDate.now().toString())
                .setInterest("3")
                .setPrinciple("10")
                .setOutstanding("90")
                .setTotal("13");
        return List.of(repay1, repay2);
    }
}
