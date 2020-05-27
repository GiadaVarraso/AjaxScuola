$(document).ready(function(){
	
	const tbody= $('#tbody');
	
	
	
	
	
	function getStudenti() {//funzione per prendere la lista degli studenti
	$.get('studenti',function(res){	//con jquery facciamo una chiamata get a studenti	
	//il res lo usiamo per iterarlo e ricavarne i tr dei singoli studenti
		for (let i = 0; i < res.length; i++) {
			$(`<tr>
					<td>${res[i].id}</td>
					<td>${res[i].nome}</td>
					<td>${res[i].cognome}</td>
					<td>${res[i].classe}</td>
					<td>${res[i].sezione}</td>
					<td>
					<button id="dettaglio" data-id="${res[i].id}">dettaglio</button>
					<button id="elimina" data-id="${res[i].id}">elimina</button>
					<button id="modifica" data-id="${res[i].id}">modifica</button></td>
				</tr>`).appendTo(tbody);//lo appendiamo al table body CHE CI ERAVAMO SALVATI PRIMA
		}		//in ogni bottone metto i data-id come attributo cosi posso ricavare l id dello studente
	})
	}
	
	getStudenti();
	
	
	$('#btn-aggiungi').click(function(){
		const studente={ //creo l oggetto in formato json con i valori contenuti negli input
				nome :    $('#nome').val(),
				cognome : $('#cognome').val(),
				email :   $('#email').val(),
				nTel :    $('#nTel').val(),
				classe :  $('#classe').val(),
				sezione : $('#sezione').val()
		}//aggiungerlo 
		
		if (editMode){
			studente.id=editId
			modificaStudente(studente)
			
		}else{
			addStudente(studente)
		}
		//pulisco gli input in ogni caso
		$('#nome').val(""),   
		$('#cognome').val("")
		$('#email').val("")  
		$('#nTel').val("")  
		$('#classe').val("")
		$('#sezione').val("") 
	})
	
	
	function addStudente(studente){//attraverso jquery effettuiamo una chiamata al post di 'studenti'
		//dove inviamo il parametro che ci arriva utilizzando JSON.stringify(parametro)
		$.post('studenti',JSON.stringify(studente), function(){
			$(tbody).html("")	
			getStudenti()
		})
	}
	
	
	tbody.on('click','#elimina',function(){ //devo prendere dal tbody perchè il resto è stato creato dinamicamente
		const id=$(this).attr('data-id')//mi salvo l id estrapolato dall attributo
		deleteStudente(id,$(this).parent().parent()) //uso la funzione deleteStudent 
		//a cui passo il nonno di this, che sarebbe il bottone (quello che fa partire l evento)
	})
	
	function deleteStudente(id,htmlRow){
	 $.ajax({
		 url: `studenti/${id}`,
		 type: 'DELETE' ,
		 success: function(res){ 
			if(res.msg==="ok"){
				htmlRow.remove()
			}
		 }		 
	 })
	}
	
	
	let editMode=false
	let editId=-1
	
	tbody.on('click','#modifica',function(){
		editMode=true //edit mode ssi setta atrue perchè siamo entrati in fase modifica
		const id=$(this).attr('data-id') //l edit id si setta sull id in questione
		editId=id
		$.get(`studenti/${id}`,function(res){ //si fa una chiamata get per recuperarne i parametri
			$('#nome').val(res.nome),   
			$('#cognome').val(res.cognome)
			$('#email').val(res.email)  
			$('#nTel').val(res.nTel)  
			$('#classe').val(res.classe)
			$('#sezione').val(res.sezione) 
			$('#btn-aggiungi').text('modifica')
			
		})
		
	})
	
	
	
	function modificaStudente(studente){
		$.ajax({
			url:'studenti',
			type:'PUT',
			data: JSON.stringify(studente),
			success: function(res){
				if(res.msg==="ok"){
					editMode=false
					editId=-1
					$('#btn-aggiungi').text('aggiungi')
					tbody.html('')
					getStudenti()
				}
			}			
		})		
	}

	
/*
 * 
		
		
		- Cliccando su un bottone visualizzare in un'altra parte della pagina 
		  il dettaglio completo dello studente
		  (BONUS provare a far apparire un "Modale" dove verranno 
		    visualizzati i dettagli) 
		  [Mettere anche un pulsante per chiudere il modale]
		
		- Dare la possibilità di inserire un nuovo studente
		  (BONUS provare a mettere la form in un Modale)
		  [Cliccando su un bottone mi viene aperto il modale che può essere
		  chiuso]
		  
		- Possibilità di modificare uno studente
		  (BONUS sempre con un Modale)
 */
	
	
	
	
	
//	<div id="box" class="w3-modal">
//	<div class="w3-modal-content">
//
//	<div class="w3-container"><!-- il div che racchiude il contenuto del modale -->
//	<!-- uno span che ha la funzione di pulsante di chiusura gestito con l
//	attributo onclick
//	 -->
//	<span onclick="document.getElementById('box').style.display='none'"
//	class="w3-button w3-display-topright">&times;</span>
//	<!-- il contenuto  &times; serve a creare una x -->
//	<p>email: </p><!-- contenuto -->
//	<p>n. Tel</p><!-- contenuto -->
//	</div>
//	</div>
//	</div>
//	<button onclick="document.getElementById('box').style.display='block'"
//	class="w3-button">dettaglio</button>
	
	function detail(studente){
	$(
		`<div id="box" class="modal-box">
			
		<div class="contenitore"><!-- il div che racchiude il contenuto del modale -->
		<!-- uno span che ha la funzione di pulsante di chiusura gestito con l
		attributo onclick
		 -->
		<span onclick="document.getElementById('box').style.display='none'"
		class="close-detail">&times;</span>
		<!-- il contenuto  &times; serve a creare una x -->
		<p>id: ${studente.id}  </p>
		<p>nome: ${studente.nome}  </p>
		<p>cognome : ${studente.cognome}  </p>
		<p>classe   ${studente.classe}    sezione   ${studente.sezione}  </p>
		<p>email: ${studente.email}  </p>
		<p>n. Tel: ${studente.nTel}  </p>
		</div>
		</div>
	 `).appendTo('#detail') 
	}
	
	
	
	tbody.on('click','#dettaglio',function(){
		$('#detail').html('')
		
		const idDettaglio=$(this).attr('data-id')
		$.get(`studenti/${idDettaglio}`,function(res){ 
			const sDettaglio={ //creo l oggetto in formato json con i valori contenuti negli input
					id : res.id,
					nome : res.nome,
					cognome : res.cognome,
					email : res.email,
					nTel : res.nTel,
					classe : res.classe,
					sezione : res.sezione
			}//aggiungerlo 
			detail(sDettaglio)
		
	})
	
	})
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
})
