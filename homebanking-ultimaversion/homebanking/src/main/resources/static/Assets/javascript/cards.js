const { createApp } = Vue;

createApp({
  data() {
    return {
      data: undefined,
      accounts: [],
      clients: [],
      loans: [],
      cards: [],
      isActive: false
    };
  },
  created() {
    this.loadData();
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
      
    }
  },
}).mount("#app");
