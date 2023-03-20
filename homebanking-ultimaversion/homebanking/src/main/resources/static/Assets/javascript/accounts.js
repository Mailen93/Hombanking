const { createApp } = Vue;

createApp({
  data() {
    return {
      data: undefined,
      accounts: [],
      clients: [],
      loans: [],
      allDateTransactions: [],
      isActive: false,
      accountType: "",
      number: ""
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
    alertCreateAccount(){
      Swal.fire({
          title: 'Choose your account type',
          showDenyButton: true,
          showCloseButton: true,
          confirmButtonText: 'Current Account',
          denyButtonText: 'Saving Account',
        }).then((result) => {
          if (result.isConfirmed) {
              this.accountType = "CURRENT"
              this.createAccount()
          } else if (result.isDenied) {
              this.accountType = "SAVING"
              this.createAccount() 
          }
        })
},
    createAccount(){
      axios.post("http://localhost:8080/api/clients/current/accounts",`accountType=${this.accountType}`)
          .then(response => { 
            this.loadData()
          })
          .catch(error => {
              this.error = error.response.data.message;
          });
  },
  deleteAccount(){
    axios.patch("http://localhost:8080/api/clients/current/accounts", `name=${this.number}`, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(response => {window.location.href = '/web/accounts.html'
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
      document.querySelector('.htopAcc').classList.toggle('openTop')
      document.querySelector('.hmidAcc').classList.toggle('openMid')
      document.querySelector('.hbotAcc').classList.toggle('openBot')
      document.querySelector('#navModal').classList.toggle('modalOpen')
      document.querySelector('#navModalAcc').classList.toggle('modalOpen')
      this.isActive = !this.isActive
      console.log(this.isActive)
      
    }
  }
}).mount("#app");
