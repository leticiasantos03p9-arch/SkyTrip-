package com.skytrip;

import android.app.*;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.*;
import org.json.*;
import java.text.NumberFormat;
import java.util.*;

public class MainActivity extends Activity {
    final int BLUE=Color.rgb(23,105,255), LIGHT=Color.rgb(234,243,255), INK=Color.rgb(16,35,79), BG=Color.rgb(247,249,255);
    LinearLayout root, content, nav; ArrayList<JSONObject> trips=new ArrayList<>(); int current=-1;
    android.content.SharedPreferences prefs;
    public void onCreate(Bundle b){super.onCreate(b); getWindow().setStatusBarColor(BLUE); getWindow().setNavigationBarColor(INK); prefs=getSharedPreferences("skytrip",0); load(); shell(); home();}
    void shell(){
        root=new LinearLayout(this); root.setOrientation(1); root.setBackgroundColor(BG);
        TextView head=new TextView(this); head.setText("✈️ SkyTrip\nSeu próximo destino começa aqui."); head.setTextColor(-1); head.setTextSize(22); head.setTypeface(null,Typeface.BOLD); head.setPadding(20,20,16,20); head.setBackgroundColor(BLUE); root.addView(head);
        nav=new LinearLayout(this); nav.setPadding(8,8,8,8); nav.setBackgroundColor(-1);
        addNav("🏠 Início",()->home()); addNav("🧳 Viagens",()->listTrips()); addNav("＋ Nova",()->newTrip());
        root.addView(nav); ScrollView scroll=new ScrollView(this); content=new LinearLayout(this); content.setOrientation(1); content.setPadding(16,12,16,24); scroll.addView(content); root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1)); setContentView(root);
    }
    void addNav(String s,Runnable r){Button b=new Button(this); b.setText(s); b.setTextSize(11); b.setAllCaps(false); b.setOnClickListener(v->r.run()); nav.addView(b,new LinearLayout.LayoutParams(0,-2,1));}
    TextView text(String s,int size,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(INK);if(bold)t.setTypeface(null,Typeface.BOLD);t.setPadding(0,6,0,6);return t;}
    void panel(){LinearLayout p=new LinearLayout(this);p.setOrientation(1);p.setPadding(16,14,16,14);p.setBackgroundColor(-1); LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.bottomMargin=12;content.addView(p,lp); active=p;}
    LinearLayout active;
    EditText field(String hint, int type){EditText e=new EditText(this);e.setSingleLine(true);e.setTextSize(15);e.setHint(hint);e.setInputType(type);active.addView(e);return e;}
    Button button(String s,Runnable r){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextColor(-1);b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(BLUE));b.setOnClickListener(v->r.run());active.addView(b);return b;}
    void home(){content.removeAllViews();panel();active.addView(text("Planeje sua próxima aventura",22,true));active.addView(text("Organize destinos, atividades e gastos em um só lugar.",14,false));active.addView(text("Viagens cadastradas: "+trips.size(),17,true));active.addView(text("Total de despesas: "+money(allTotal()),17,true));button("＋ Criar viagem",()->newTrip());panel();active.addView(text("Suas viagens",19,true));if(trips.isEmpty())active.addView(text("Você ainda não tem viagens. Crie a primeira! ✈️",15,false));else for(int i=0;i<trips.size();i++)tripCard(i);}
    void listTrips(){content.removeAllViews();panel();active.addView(text("Minhas viagens",22,true));active.addView(text("Acesse os detalhes, roteiros e despesas.",14,false));if(trips.isEmpty())active.addView(text("Nenhuma viagem cadastrada ainda.",15,false));else for(int i=0;i<trips.size();i++)tripCard(i);button("＋ Nova viagem",()->newTrip());}
    void tripCard(int i){JSONObject t=trips.get(i);panel();active.addView(text("✈️ "+t.optString("dest"),19,true));active.addView(text(t.optString("start","Data não definida")+(t.optString("end").isEmpty()?"":" → "+t.optString("end")),14,false));active.addView(text(t.optInt("people",1)+" viajante(s) · "+arr(t,"activities").length()+" atividade(s)",14,false));active.addView(text("Despesas: "+money(total(t)),16,true));button("Abrir viagem",()->detail(i));}
    void newTrip(){content.removeAllViews();panel();active.addView(text("Nova viagem",22,true));active.addView(text("Preencha os detalhes para começar.",14,false));EditText dest=field("Destino (obrigatório)",android.text.InputType.TYPE_CLASS_TEXT);EditText start=field("Data de ida (AAAA-MM-DD)",android.text.InputType.TYPE_CLASS_TEXT);EditText end=field("Data de volta (AAAA-MM-DD)",android.text.InputType.TYPE_CLASS_TEXT);EditText people=field("Número de viajantes",android.text.InputType.TYPE_CLASS_NUMBER);people.setText("1");EditText budget=field("Orçamento em R$",android.text.InputType.TYPE_CLASS_NUMBER|android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);button("Salvar viagem",()->{String d=dest.getText().toString().trim();if(d.isEmpty()){dest.setError("Informe o destino");return;}try{JSONObject t=new JSONObject();t.put("id",System.currentTimeMillis());t.put("dest",d);t.put("start",start.getText().toString());t.put("end",end.getText().toString());t.put("people",Math.max(1,parseInt(people.getText().toString(),1)));t.put("budget",parseDouble(budget.getText().toString()));t.put("activities",new JSONArray());t.put("expenses",new JSONArray());trips.add(0,t);save();detail(0);}catch(Exception ex){}});}
    void detail(int i){current=i;JSONObject t=trips.get(i);content.removeAllViews();panel();active.addView(text(t.optString("dest"),23,true));active.addView(text(t.optString("start","Data não definida")+(t.optString("end").isEmpty()?"":" → "+t.optString("end"))+" · "+t.optInt("people",1)+" viajante(s)",14,false));active.addView(text("Total de despesas: "+money(total(t)),18,true));active.addView(text("Orçamento: "+money(t.optDouble("budget")),16,true));
      panel();active.addView(text("🗓️ Roteiro",19,true));EditText an=field("Nome da atividade",android.text.InputType.TYPE_CLASS_TEXT);EditText ad=field("Data (opcional)",android.text.InputType.TYPE_CLASS_TEXT);button("Adicionar atividade",()->{if(an.getText().toString().trim().isEmpty()){an.setError("Informe a atividade");return;}try{JSONObject a=new JSONObject();a.put("name",an.getText().toString().trim());a.put("date",ad.getText().toString());arr(t,"activities").put(a);save();detail(i);}catch(Exception e){}});JSONArray acts=arr(t,"activities");for(int j=0;j<acts.length();j++){final int k=j;JSONObject a=acts.optJSONObject(j);active.addView(text("• "+a.optString("name")+" — "+a.optString("date"),15,false));smallButton("Remover atividade",()->{arr(t,"activities").remove(k);save();detail(i);});}
      panel();active.addView(text("💳 Despesas",19,true));EditText en=field("Descrição (ex.: passagem)",android.text.InputType.TYPE_CLASS_TEXT);EditText ea=field("Valor em R$",android.text.InputType.TYPE_CLASS_NUMBER|android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);button("Adicionar despesa",()->{if(en.getText().toString().trim().isEmpty()||parseDouble(ea.getText().toString())<=0){en.setError("Informe descrição e valor");return;}try{JSONObject x=new JSONObject();x.put("name",en.getText().toString().trim());x.put("amount",parseDouble(ea.getText().toString()));arr(t,"expenses").put(x);save();detail(i);}catch(Exception e){}});JSONArray exps=arr(t,"expenses");for(int j=0;j<exps.length();j++){final int k=j;JSONObject x=exps.optJSONObject(j);active.addView(text(x.optString("name")+" — "+money(x.optDouble("amount")),15,false));smallButton("Remover despesa",()->{arr(t,"expenses").remove(k);save();detail(i);});}
      panel();button("🗑️ Excluir viagem",()->new AlertDialog.Builder(this).setTitle("Excluir viagem?").setMessage("Esta ação não pode ser desfeita.").setNegativeButton("Cancelar",null).setPositiveButton("Excluir",(d,w)->{trips.remove(i);save();listTrips();}).show());button("← Voltar às viagens",()->listTrips());
    }
    void smallButton(String s,Runnable r){Button b=new Button(this);b.setText(s);b.setAllCaps(false);b.setTextColor(BLUE);b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(LIGHT));b.setOnClickListener(v->r.run());active.addView(b);}
    JSONArray arr(JSONObject t,String key){JSONArray a=t.optJSONArray(key);if(a==null){a=new JSONArray();try{t.put(key,a);}catch(Exception e){}}return a;}
    double total(JSONObject t){double n=0;JSONArray a=arr(t,"expenses");for(int i=0;i<a.length();i++)n+=a.optJSONObject(i).optDouble("amount");return n;}
    double allTotal(){double n=0;for(JSONObject t:trips)n+=total(t);return n;}
    String money(double n){return NumberFormat.getCurrencyInstance(new Locale("pt","BR")).format(n);}
    int parseInt(String s,int d){try{return Integer.parseInt(s);}catch(Exception e){return d;}}
    double parseDouble(String s){try{return Double.parseDouble(s.replace(",","."));}catch(Exception e){return 0;}}
    void save(){JSONArray a=new JSONArray();for(JSONObject t:trips)a.put(t);prefs.edit().putString("trips",a.toString()).apply();}
    void load(){try{JSONArray a=new JSONArray(prefs.getString("trips","[]"));for(int i=0;i<a.length();i++)trips.add(a.getJSONObject(i));}catch(Exception ignored){}}
}