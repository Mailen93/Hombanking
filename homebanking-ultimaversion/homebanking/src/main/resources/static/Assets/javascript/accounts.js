const { createApp } = Vue;

createApp({
  data() {
    return {
      data: undefined,
      accounts: [],
      clients: [],
      loans: [],
      allDateTransactions: [],
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
          this.allTransactions();
          this.allDateTransactions = this.allDateTransactions.sort((a,b) => new Date(b.date).getTime() - new Date(a.date).getTime())
          console.log(this.allDateTransactions)
        })
        .catch((err) => console.log(err));
    },
    parseDate(fecha) {
      let date = fecha.split("T")[0];
      let newDate = date.split("-").reverse().join("/");
      return newDate;
    },
    allTransactions(){
      for (account of this.accounts){
        for (transaction of account.transaction){
          this.allDateTransactions.push(transaction)
        }
      }
    },
    createAccount(){
      axios.post("http://localhost:8080/api/clients/current/accounts") 
          .then(response => { 
            this.loadData()
          })
          .catch(error => {
              this.error = error.response.data.message;
          });
  },
    logOut(){             
      axios.post("http://localhost:8080/api/logout")
      .then((response) =>{        
      window.location.href = '/web/index.html';
    })
    .catch((err) => console.log(hola));
    },
    h: function(){
      document.querySelector('.htop').classList.toggle('openTop')
      document.querySelector('.hmid').classList.toggle('openMid')
      document.querySelector('.hbot').classList.toggle('openBot')
      document.querySelector('#modal').classList.toggle('modalOpen')
      
    }
  },
}).mount("#app");
