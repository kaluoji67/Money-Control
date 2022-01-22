package com.example.pluralcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link transactionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class transactionsFragment extends Fragment implements MyAdapter.ClickListener {

    /////////////////////////////////////
    FirebaseUser user;
    String userID;
    //////////////////////////////////////
    String projectName;

    private RecyclerView mRecyclerView;
    private ArrayList<Transaction>transactions;
    private ArrayList<String> keysList;
    private MyAdapter adapter;
    private SearchView searchView;
    DatabaseReference myRef;
    String key;

    //filter objects


    private  String currentSearchText = "";
    private ArrayList<String> selectedFilters = new ArrayList<String>();

    Button filterButton;
    ConstraintLayout filterContainer;
    Button all, incomeType,expensesType;

    TextView incomeCatShow, expensesCatShow;
    String visibleCat ="income";

    RelativeLayout incomeCatRelLay, expensesCatRelLay;
    Button salaryBtt, loanBtt, investmentBtt, otherIncomeBttn;

    Button groceriesBtt, entertainmentBtt, foodBtt, householdBtt, educationBtt,giftBtt, otherExpensesBtt, healthBtt;

    Button cashBtt, bankBtt;

    Button back;

    //colors
    int purple;
    int grey;

    //Export pdf

    Button export;
    WebView pdfWebView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public transactionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment transactionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static transactionsFragment newInstance(String param1, String param2) {
        transactionsFragment fragment = new transactionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            projectName=mParam1;
        }
       //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen


        View rootView = inflater.inflate(R.layout.fragment_transactions, container, false);

        final Context context = getActivity().getApplicationContext();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_transactions);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        keysList=new ArrayList<String>();
        transactions=new ArrayList<Transaction>();
        searchView = (SearchView) rootView.findViewById(R.id.search_view);
        pdfWebView=rootView.findViewById(R.id.pdf_webview);
        export=rootView.findViewById(R.id.export_Button);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();


        myRef= FirebaseDatabase.getInstance("https://money-control-c1462-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("users").child(userID).child(projectName).child("transactions");
        myRef.keepSynced(true);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                    Transaction t=npsnapshot.getValue(Transaction.class);
                    String key = npsnapshot.getKey()+"";
                    Log.d("keys", key);

                    transactions.add(t);
                    keysList.add(key);
                }
                /// error
                adapter=new MyAdapter(transactions,transactionsFragment.this);
                mRecyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //initialize filter objects

            //Type filter
        filterButton = rootView.findViewById(R.id.filter_button);
        filterContainer = rootView.findViewById(R.id.filter_container);
        incomeType = rootView.findViewById(R.id.income_type_button);
        expensesType = rootView.findViewById(R.id.expenses_type_button);

            //Category filter
        incomeCatShow = rootView.findViewById(R.id.income_cat_show);
        expensesCatShow = rootView.findViewById(R.id.expenses_cat_show);
        incomeCatRelLay = rootView.findViewById(R.id.incomeCategoryRelativeLayout);
        expensesCatRelLay = rootView.findViewById(R.id.expensesCategoryRelativeLayout);

        salaryBtt= rootView.findViewById(R.id.salary_button);
        loanBtt = rootView.findViewById(R.id.loan_button);
        investmentBtt=rootView.findViewById(R.id.investment_button);
        otherIncomeBttn = rootView.findViewById(R.id.other_income_button);

        groceriesBtt= rootView.findViewById(R.id.groceries_button);
        entertainmentBtt= rootView.findViewById(R.id.entertainment_button);
        foodBtt= rootView.findViewById(R.id.food_button);
        householdBtt= rootView.findViewById(R.id.household_button);
        educationBtt= rootView.findViewById(R.id.education_button);
        giftBtt= rootView.findViewById(R.id.gift_button);
        otherExpensesBtt= rootView.findViewById(R.id.other_button);
        healthBtt = rootView.findViewById(R.id.health_button);

        cashBtt= rootView.findViewById(R.id.cash_button);
        bankBtt= rootView.findViewById(R.id.bank_button);


        all = rootView.findViewById(R.id.all_type_button);

        back = rootView.findViewById(R.id.back_button);

        //FILTER ACTION BUTTON
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterContainer.getVisibility()== View.VISIBLE){
                    filterContainer.setVisibility(View.GONE);
                }else{
                    filterContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        incomeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeTypeTapped();
            }
        });

        expensesType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expensesTypeTapped();
            }
        });


        //reset
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTapped();
            }
        });

        incomeCatShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visibleCat!="income"){
                    incomeCatRelLay.setVisibility(View.VISIBLE);
                    expensesCatRelLay.setVisibility(View.GONE);
                    visibleCat="income";
                    incomeCatShow.setAlpha(1.0f);
                    expensesCatShow.setAlpha(0.2f);
                }
            }
        });

        expensesCatShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visibleCat=="income"){
                    incomeCatRelLay.setVisibility(View.GONE);
                    expensesCatRelLay.setVisibility(View.VISIBLE);
                    visibleCat="expenses";
                    incomeCatShow.setAlpha(0.2f);
                    expensesCatShow.setAlpha(1.0f);
                }
            }
        });

        salaryBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salaryTapped();
            }
        });

        loanBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanTapped();
            }
        });

        investmentBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                investmentTapped();
            }
        });

        otherIncomeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherIncomeTapped();
            }
        });

        foodBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodTapped();
            }
        });

        healthBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                healthTapped();
            }
        });

        groceriesBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groceriesTapped();
            }
        });

        entertainmentBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entertainmentTapped();
            }
        });

        householdBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                householdTapped();
            }
        });

        educationBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                educationTapped();
            }
        });

        giftBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giftTapped();
            }
        });

        otherExpensesBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherExpensesTapped();
            }
        });

        cashBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashTapped();
            }
        });

        bankBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankTapped();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterContainer.setVisibility(View.GONE);
            }
        });


        //EXPORT
        export.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(!transactions.isEmpty()){
                    String top ="<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "\t<title></title>\n" +
                            "\t<link rel=\"stylesheet\" type=\"text/css\" href=\"file:android_asset/CreatePdf.css\">\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "\t<div>\n" +
                            "\t\t<h3>View My Transactions</h3>\n" +
                            "\t\t<table border=\"1\">\n" +
                            "\t\t\t<tr>\n" +
                            "\t\t\t\t<th> S\\N </th>\n" +
                            "\t\t\t\t<th> Name </th>\n" +
                            "\t\t\t\t<th> Type </th>\n" +
                            "\t\t\t\t<th> Category </th>\n" +
                            "\t\t\t\t<th> Amount </th>\n" +
                            "\t\t\t\t<th> Date </th>\n" +
                            "\t\t\t\t<th> Time </th>\n" +
                            "\t\t\t</tr>\n";

                    int count = 0;
                    String middle="";

                    for(Transaction t: transactions){
                        middle += "\t\t\t<tr>\n" +
                                "\t\t\t\t<td>" + (count+1) + " </td>\n" +
                                "\t\t\t\t<td>" + t.getDescription() + " </td>\n" +
                                "\t\t\t\t<td>" + t.getType() + " </td>\n" +
                                "\t\t\t\t<td>" + t.getCategory() + " </td>\n" +
                                "\t\t\t\t<td>" + t.getCurrency() + t.getAmount() + " </td>\n" +
                                "\t\t\t\t<td>" + t.getDate() + " </td>\n" +
                                "\t\t\t\t<td>" + t.getTime() + " </td>\n" +
                                "\t\t\t</tr>\n";
                    }

                    String end = "\t\t</table>\n" +
                            "\t</div>\n" +
                            "\n" +
                            "</body>\n" +
                            "</html>";

                    String html = top + middle + end;

                    pdfWebView.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
                    createPdf();

                }else{
                    Toast.makeText(context,"No entries to export", Toast.LENGTH_LONG).show();
                }
            }
        });




        return rootView;
    }

    //END OF ONCREATE

    @Override
    public void onStart() {
        super.onStart();
        if(searchView!=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    currentSearchText = newText;
                    if(newText.equals("")){
                        if(selectedFilters.isEmpty()){
                            MyAdapter filteredAdapter=new MyAdapter(transactions,transactionsFragment.this);
                            mRecyclerView.setAdapter(filteredAdapter);
                        }else{
                            for(String filter: selectedFilters){
                                filterList(filter);
                            }
                        }


                    }else{
                        search(newText);
                    }

                    return true;
                }
            });

        }
    }

    //EXPORT
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPdf(){
        Context context=getActivity().getApplicationContext();
        PrintManager printManager=(PrintManager)getActivity().getSystemService(context.PRINT_SERVICE);
        PrintDocumentAdapter adapter=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            adapter=pdfWebView.createPrintDocumentAdapter();
        }
        String JobName=getString(R.string.app_name) +"Document";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            PrintJob printJob=printManager.print(JobName,adapter,new PrintAttributes.Builder().build());
        }

    }


    //START SEARCH AND FILTER
    private void search(String str){
        ArrayList<Transaction> filteredTrans= new ArrayList<>();
        for(Transaction transaction: transactions){
            if(transaction.getCategory().toLowerCase().contains(str.toLowerCase())
                    || transaction.getPayment_type().toLowerCase().contains(str.toLowerCase())
                    || transaction.getDescription().toLowerCase().contains(str.toLowerCase())
                    || transaction.getType().toLowerCase().contains(str.toLowerCase())){

                if(selectedFilters.isEmpty()){
                    filteredTrans.add(transaction);
                }else{
                    for(String filter: selectedFilters){
                        if(transaction.getCategory().toLowerCase().contains(filter.toLowerCase())
                                || transaction.getPayment_type().toLowerCase().contains(filter.toLowerCase())
                                || transaction.getType().toLowerCase().contains(filter.toLowerCase())){
                            filteredTrans.add(transaction);
                        }
                    }
                }
            }


        }
        MyAdapter filteredAdapter=new MyAdapter(filteredTrans,this);
        mRecyclerView.setAdapter(filteredAdapter);
    }

    private void initColors()
    {
        grey = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.mylightgrey);
        purple = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.app_color_theme);
    }

    private void unSelectAllFilterButtons()
    {
        lookUnSelected(incomeType);
        lookUnSelected(expensesType);
        lookUnSelected(foodBtt);
        lookUnSelected(healthBtt);
        lookUnSelected(groceriesBtt);
        lookUnSelected(entertainmentBtt);
        lookUnSelected(householdBtt);
        lookUnSelected(educationBtt);
        lookUnSelected(giftBtt);
        lookUnSelected(otherExpensesBtt);
        lookUnSelected(bankBtt);
        lookUnSelected(cashBtt);

        lookUnSelected(otherIncomeBttn);
        lookUnSelected(investmentBtt);
        lookUnSelected(salaryBtt);
        lookUnSelected(loanBtt);
    }


    private void lookSelected(Button parsedButton)
    {
        parsedButton.setAlpha(1f);
    }
    
    private void lookUnSelected(Button parsedButton)
    {
        parsedButton.setAlpha(0.2f);
    }

    private void filterList(String status)
    {
        if(status != null && !selectedFilters.contains(status))
            selectedFilters.add(status);

        ArrayList<Transaction> filteredTrans= new ArrayList<>();

        for(Transaction t: transactions)
        {
            for(String filter: selectedFilters)
            {
                // Have filters for different sets of filter i.e by Type, category and payment

                // for Type
                if(t.getType().toLowerCase().contains(filter.toLowerCase()))
                {
                    if(currentSearchText == "")
                    {
                        //To avoid repetitions of intersecting searches
                        if(!filteredTrans.contains(t)){
                            filteredTrans.add(t);
                        }

                    }
                    else
                    {
                        if(t.getCategory().toLowerCase().contains(currentSearchText.toLowerCase())
                        || t.getType().toLowerCase().contains(currentSearchText.toLowerCase())
                        || t.getDescription().toLowerCase().contains(currentSearchText.toLowerCase())
                        || t.getPayment_type().toLowerCase().contains(currentSearchText.toLowerCase())
                        ) {
                            filteredTrans.add(t);
                        }
                    }
                }

                // for category

                if(t.getCategory().toLowerCase().contains(filter))
                {
                    if(currentSearchText == "")
                    {
                        //To avoid repetitions of intersecting searches
                        if(!filteredTrans.contains(t)){
                            filteredTrans.add(t);
                        }
                    }
                    else
                    {
                        if(t.getCategory().toLowerCase().contains(currentSearchText.toLowerCase()))
                        {
                            filteredTrans.add(t);
                        }
                    }
                }

                //for payment

                if(t.getPayment_type().toLowerCase().contains(filter))
                {
                    if(currentSearchText == "")
                    {
                        //To avoid repetitions of intersecting searches
                        if(!filteredTrans.contains(t)){
                            filteredTrans.add(t);
                        }
                    }
                    else
                    {
                        if(t.getPayment_type().toLowerCase().contains(currentSearchText.toLowerCase()))
                        {
                            filteredTrans.add(t);
                        }
                    }
                }


            }
        }

        MyAdapter filteredAdapter=new MyAdapter(filteredTrans,this);
        mRecyclerView.setAdapter(filteredAdapter);
    }


    //set what happens for each filter button click

    //This can be used to clear all filters and show all
    public void allTapped()
    {
        selectedFilters.clear();
        unSelectAllFilterButtons();

        if(currentSearchText==""){
            MyAdapter filteredAdapter=new MyAdapter(transactions,this);
            mRecyclerView.setAdapter(filteredAdapter);
        }else{
            search(currentSearchText);
        }


    }

    public void incomeTypeTapped()
    {
        //if it has already been tapped
        if(selectedFilters.contains("income")){
            lookUnSelected(incomeType);
            selectedFilters.remove("income");

            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }

        }

        //if not already tapped
        else{
            filterList("income");
            lookSelected(incomeType);
        }
    }

    public void expensesTypeTapped()
    {
        if(selectedFilters.contains("expenses")){
            lookUnSelected(expensesType);
            selectedFilters.remove("expenses");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("expenses");
            lookSelected(expensesType);
        }
    }

    public void salaryTapped()
    {
        if(selectedFilters.contains("salary")){
            lookUnSelected(salaryBtt);
            selectedFilters.remove("salary");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("salary");
            lookSelected(salaryBtt);
        }
    }

    public void loanTapped()
    {
        if(selectedFilters.contains("loan")){
            lookUnSelected(loanBtt);
            selectedFilters.remove("loan");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("loan");
            lookSelected(loanBtt);
        }
    }

    public void investmentTapped()
    {
        if(selectedFilters.contains("investment")){
            lookUnSelected(investmentBtt);
            selectedFilters.remove("investment");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("investment");
            lookSelected(investmentBtt);
        }
    }

    public void otherIncomeTapped()
    {
        if(selectedFilters.contains("other")){
            lookUnSelected(otherIncomeBttn);
            selectedFilters.remove("other");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("other");
            lookSelected(otherIncomeBttn);
        }
    }

    public void foodTapped()
    {
        if(selectedFilters.contains("food")){
            lookUnSelected(foodBtt);
            selectedFilters.remove("food");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("food");
            lookSelected(foodBtt);
        }
    }

    public void healthTapped()
    {
        if(selectedFilters.contains("health")){
            lookUnSelected(otherIncomeBttn);
            selectedFilters.remove("health");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("health");
            lookSelected(healthBtt);
        }
    }

    public void groceriesTapped()
    {
        if(selectedFilters.contains("groceries")){
            lookUnSelected(groceriesBtt);
            selectedFilters.remove("groceries");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("groceries");
            lookSelected(groceriesBtt);
        }
    }

    public void entertainmentTapped()
    {
        if(selectedFilters.contains("entertainment")){
            lookUnSelected(entertainmentBtt);
            selectedFilters.remove("entertainment");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("entertainment");
            lookSelected(entertainmentBtt);
        }
    }

    public void householdTapped()
    {
        if(selectedFilters.contains("household")){
            lookUnSelected(householdBtt);
            selectedFilters.remove("household");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("household");
            lookSelected(householdBtt);
        }
    }

    public void educationTapped()
    {
        if(selectedFilters.contains("education")){
            lookUnSelected(educationBtt);
            selectedFilters.remove("education");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("education");
            lookSelected(educationBtt);
        }
    }

    public void giftTapped()
    {
        if(selectedFilters.contains("gift")){
            lookUnSelected(giftBtt);
            selectedFilters.remove("gift");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("gift");
            lookSelected(giftBtt);
        }
    }

    public void otherExpensesTapped()
    {
        if(selectedFilters.contains("other")){
            lookUnSelected(otherExpensesBtt);
            selectedFilters.remove("other");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("other");
            lookSelected(otherExpensesBtt);
        }
    }

    public void cashTapped()
    {
        if(selectedFilters.contains("cash")){
            lookUnSelected(cashBtt);
            selectedFilters.remove("cash");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("cash");
            lookSelected(cashBtt);
        }
    }

    public void bankTapped()
    {
        if(selectedFilters.contains("bank transfer")){
            lookUnSelected(bankBtt);
            selectedFilters.remove("bank");
            if(!selectedFilters.isEmpty()){
                for(String filter: selectedFilters){
                    filterList(filter);
                }
            }else{
                allTapped();
            }
        }
        //if not tapped
        else{
            filterList("bank");
            lookSelected(bankBtt);
        }
    }







    /// what happens when you click a transaction  //////
    ////////////////////////////////////////////////////
    @Override
    public void onItemClick(int position) {
        key = keysList.get(position);

        //Intent i = new Intent(getActivity(),deletePopUp.class);
        //i.putExtra("keyToDelete",key);
        Intent i = new Intent(getActivity(),addActivity.class);
        i.putExtra("keyToDelete",key);
        i.putExtra("projectName",projectName);
        startActivity(i);

    }

    public void delete(String key){
        myRef.child(key).removeValue();

        keysList.clear();
        transactions.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot npsnapshot : snapshot.getChildren()){
                    Transaction t=npsnapshot.getValue(Transaction.class);
                    String key = npsnapshot.getKey()+"";
                    keysList.add(key);
                    transactions.add(t);
                }
                adapter=new MyAdapter(transactions,transactionsFragment.this);
                mRecyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemLongClick(int position, View v) {

    }
}