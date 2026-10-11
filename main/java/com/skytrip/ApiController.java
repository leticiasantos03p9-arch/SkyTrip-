package com.skytrip;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {
 private final PessoaRepository pessoas; private final ViagemRepository viagens;
 private final AtividadeRepository atividades; private final GastoRepository gastos; private final PasswordEncoder encoder;
 public ApiController(PessoaRepository p,ViagemRepository v,AtividadeRepository a,GastoRepository g,PasswordEncoder e){pessoas=p;viagens=v;atividades=a;gastos=g;encoder=e;}
 record AuthInput(String nome,String email,String senha){}
 record ViagemInput(String titulo,String destino,String dataInicio,String dataFim,Integer viajantes){}
 record AtividadeInput(String descricao,String data){}
 record GastoInput(String descricao,BigDecimal valor){}
 private Pessoa atual(HttpSession s){Object id=s.getAttribute("pessoaId"); if(id==null)return null; return pessoas.findById((Long)id).orElse(null);}
 private ResponseEntity<?> erro(String msg,HttpStatus status){return ResponseEntity.status(status).body(Map.of("erro",msg));}
 @PostMapping("/cadastro") public ResponseEntity<?> cadastro(@RequestBody AuthInput in,HttpSession s){
  if(in.nome()==null||in.nome().isBlank()||in.email()==null||!in.email().contains("@")||in.senha()==null||in.senha().length()<6)return erro("Informe nome, e-mail válido e senha com pelo menos 6 caracteres.",HttpStatus.BAD_REQUEST);
  String email=in.email().trim().toLowerCase(Locale.ROOT); if(pessoas.findByEmailIgnoreCase(email).isPresent())return erro("Este e-mail já está cadastrado.",HttpStatus.CONFLICT);
  Pessoa p=pessoas.save(new Pessoa(in.nome().trim(),email,encoder.encode(in.senha())));s.setAttribute("pessoaId",p.id);return ResponseEntity.ok(Map.of("nome",p.nome,"email",p.email));
 }
 @PostMapping("/login") public ResponseEntity<?> login(@RequestBody AuthInput in,HttpSession s){
  if(in.email()==null||in.senha()==null)return erro("Informe e-mail e senha.",HttpStatus.BAD_REQUEST);
  Pessoa p=pessoas.findByEmailIgnoreCase(in.email().trim()).orElse(null);if(p==null||!encoder.matches(in.senha(),p.senhaHash))return erro("E-mail ou senha incorretos.",HttpStatus.UNAUTHORIZED);
  s.setAttribute("pessoaId",p.id);return ResponseEntity.ok(Map.of("nome",p.nome,"email",p.email));
 }
 @PostMapping("/logout") public ResponseEntity<?> logout(HttpSession s){s.invalidate();return ResponseEntity.ok(Map.of("ok",true));}
 @GetMapping("/me") public ResponseEntity<?> me(HttpSession s){Pessoa p=atual(s);return p==null?erro("Faça login.",HttpStatus.UNAUTHORIZED):ResponseEntity.ok(Map.of("nome",p.nome,"email",p.email));}
 @GetMapping("/viagens") public ResponseEntity<?> listar(HttpSession s){Pessoa p=atual(s);if(p==null)return erro("Faça login.",HttpStatus.UNAUTHORIZED);return ResponseEntity.ok(viagens.findByPessoaIdOrderByIdDesc(p.id).stream().map(this::dto).toList());}
 @PostMapping("/viagens") public ResponseEntity<?> criar(@RequestBody ViagemInput in,HttpSession s){
  Pessoa p=atual(s);if(p==null)return erro("Faça login.",HttpStatus.UNAUTHORIZED);
  if(in.destino()==null||in.destino().isBlank())return erro("Informe o destino.",HttpStatus.BAD_REQUEST);
  Viagem v=new Viagem();v.pessoa=p;v.titulo=(in.titulo()==null||in.titulo().isBlank())?in.destino().trim():in.titulo().trim();v.destino=in.destino().trim();v.dataInicio=parseDate(in.dataInicio());v.dataFim=parseDate(in.dataFim());v.viajantes=in.viajantes()==null||in.viajantes()<1?1:in.viajantes();return ResponseEntity.ok(dto(viagens.save(v)));
 }
 private java.time.LocalDate parseDate(String d){try{return d==null||d.isBlank()?null:java.time.LocalDate.parse(d);}catch(Exception e){return null;}}
 private Viagem owned(Long id,HttpSession s){Pessoa p=atual(s);if(p==null)return null;return viagens.findById(id).filter(v->v.pessoa.id.equals(p.id)).orElse(null);}
 private Map<String,Object> dto(Viagem v){var acts=atividades.findByViagemIdOrderByIdAsc(v.id);var costs=gastos.findByViagemIdOrderByIdAsc(v.id);BigDecimal total=costs.stream().map(g->g.valor).reduce(BigDecimal.ZERO,BigDecimal::add);return Map.of("id",v.id,"titulo",v.titulo,"destino",v.destino,"dataInicio",v.dataInicio==null?"":v.dataInicio.toString(),"dataFim",v.dataFim==null?"":v.dataFim.toString(),"viajantes",v.viajantes==null?1:v.viajantes,"atividades",acts.stream().map(a->Map.of("id",a.id,"descricao",a.descricao,"data",a.data==null?"":a.data)).toList(),"gastos",costs.stream().map(g->Map.of("id",g.id,"descricao",g.descricao,"valor",g.valor)).toList(),"total",total);}
 @DeleteMapping("/viagens/{id}") public ResponseEntity<?> excluir(@PathVariable Long id,HttpSession s){Viagem v=owned(id,s);if(v==null)return erro("Viagem não encontrada.",HttpStatus.NOT_FOUND);atividades.deleteByViagemId(id);gastos.deleteByViagemId(id);viagens.delete(v);return ResponseEntity.ok(Map.of("ok",true));}
 @PostMapping("/viagens/{id}/atividades") public ResponseEntity<?> addAtividade(@PathVariable Long id,@RequestBody AtividadeInput in,HttpSession s){Viagem v=owned(id,s);if(v==null)return erro("Viagem não encontrada.",HttpStatus.NOT_FOUND);if(in.descricao()==null||in.descricao().isBlank())return erro("Informe a atividade.",HttpStatus.BAD_REQUEST);Atividade a=new Atividade();a.viagem=v;a.descricao=in.descricao().trim();a.data=in.data();atividades.save(a);return ResponseEntity.ok(dto(v));}
 @DeleteMapping("/atividades/{id}") public ResponseEntity<?> delAtividade(@PathVariable Long id,HttpSession s){Pessoa p=atual(s);Atividade a=atividades.findById(id).orElse(null);if(p==null)return erro("Faça login.",HttpStatus.UNAUTHORIZED);if(a==null||!a.viagem.pessoa.id.equals(p.id))return erro("Atividade não encontrada.",HttpStatus.NOT_FOUND);Long vid=a.viagem.id;atividades.delete(a);return ResponseEntity.ok(dto(viagens.findById(vid).orElseThrow()));}
 @PostMapping("/viagens/{id}/gastos") public ResponseEntity<?> addGasto(@PathVariable Long id,@RequestBody GastoInput in,HttpSession s){Viagem v=owned(id,s);if(v==null)return erro("Viagem não encontrada.",HttpStatus.NOT_FOUND);if(in.descricao()==null||in.descricao().isBlank()||in.valor()==null||in.valor().compareTo(BigDecimal.ZERO)<0)return erro("Informe descrição e valor válido.",HttpStatus.BAD_REQUEST);Gasto g=new Gasto();g.viagem=v;g.descricao=in.descricao().trim();g.valor=in.valor();gastos.save(g);return ResponseEntity.ok(dto(v));}
 @DeleteMapping("/gastos/{id}") public ResponseEntity<?> delGasto(@PathVariable Long id,HttpSession s){Pessoa p=atual(s);Gasto g=gastos.findById(id).orElse(null);if(p==null)return erro("Faça login.",HttpStatus.UNAUTHORIZED);if(g==null||!g.viagem.pessoa.id.equals(p.id))return erro("Gasto não encontrado.",HttpStatus.NOT_FOUND);Long vid=g.viagem.id;gastos.delete(g);return ResponseEntity.ok(dto(viagens.findById(vid).orElseThrow()));}
}