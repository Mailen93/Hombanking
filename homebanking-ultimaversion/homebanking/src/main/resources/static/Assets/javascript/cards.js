const { createApp } = Vue;

createApp({
  data() {
    return {
      data: undefined,
      accounts: [],
      clients: [],
      loans: [],
      cards: [],
      isActive: false,
      setModal: 0,
      fechaAlReves:"",
      number:""
    };
  },
  created() {
    this.loadData();
    this.tarjetaVencida();
  },
  methods: {
    loadData() {
      axios
        .get("http://localhost:8080/api/clients/current")
        .then((response) => {
          this.data = response;
          this.accounts = response.data.accounts;
          this.clients = response.data;
          this.loans = response.data.loans;
          this.cards = response.data.cards
          console.log(this.cards)
        })
        .catch((err) => console.log(err));
    },
    parseDate(fecha) {
      let date = fecha.split("T")[0];
      let newDate = date.split("-").reverse().join("/");
      return newDate;
    },
    tarjetaVencida(){
      let vencida = new Date();
      let opciones = {year: "numeric", month: "2-digit", day: "2-digit"};
      let fechaFormateada = vencida.toLocaleString("es-ES",opciones);
      this.fechaAlReves = fechaFormateada.split("/").reverse().join("-");
    },
    deleteCard(){
      console.log("hola")
      axios.patch('/api/clients/current/cards', `number=${this.number}`, {
          headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
          }
      })
      .then(response => {window.location.href = '/web/cards.html'
  })
  },
    logOut(){      
      Swal.fire({
        title: 'Do you want to log out?',
        text: "Please select an option",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes'
      })
      .then((result) => {
        if (result.isConfirmed) {
          axios.post("http://localhost:8080/api/logout")
          .then((response) =>{        
          window.location.href = '/web/index.html';
        })
        .catch(error => {
           this.error = error.response.data;
        })
        }})
    },
    h: function(){
      document.querySelector('.htop').classList.toggle('openTop')
      document.querySelector('.hmid').classList.toggle('openMid')
      document.querySelector('.hbot').classList.toggle('openBot')
      document.querySelector('#navModal').classList.toggle('modalOpen')
      this.isActive = !this.isActive  
    },
    setModalType(num) {
      this.setModal = num
      console.log(this.setModal)
    },
    modalTitle() {
      if(this.setModal === 1) return "SILVER CARD"
      if(this.setModal === 2) return "GOLD CARD"
      if(this.setModal === 3) return "TITANIUM CARD"
    },
    modalBody(){
      if(this.setModal === 1)return "Having a Silver card can offer a range of benefits for consumers. These credit cards come with lower interest rates and higher credit limits than standard cards. Additionally, may offer rewards programs, such as cashback or travel points, which can help cardholders save money or earn valuable perks. Silver cards may also offer purchase protection and fraud prevention features, giving cardholders added peace of mind when making purchases. Overall, a silver credit card can be a valuable financial tool for those looking to build credit or access additional benefits and rewards."
      if(this.setModal === 2)return "A Gold card offers even more benefits than a silver card. In addition to higher credit limits and lower interest rates, gold credit cards come with additional perks like extended warranties, travel insurance, and concierge services. Cardholders may also enjoy higher rewards earning rates and more exclusive rewards programs. Gold cards may also offer enhanced security features like EMV chip technology and fraud detection. Overall, a gold card can provide significant value for frequent travelers, big spenders, and those looking for extra perks and protection."
      if(this.setModal === 3)return "A Titanium card is a premium option that offers a range of benefits beyond what silver or gold cards provide. Titanium credit cards often come with the highest credit limits and lowest interest rates, making them ideal for big spenders who need significant purchasing power. Additionally, titanium credit cards may offer even more exclusive rewards programs, such as access to luxury travel lounges or personalized concierge services. Cardholders may also enjoy premium perks like complimentary travel insurance, priority boarding, and airport lounge access. With top-of-the-line security features and premium customer service, a titanium credit card can provide unparalleled benefits and value for high-net-worth individuals and frequent travelers."
    },
  },
}).mount("#app");
