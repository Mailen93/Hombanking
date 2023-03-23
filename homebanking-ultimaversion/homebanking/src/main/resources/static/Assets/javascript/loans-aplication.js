const { createApp } = Vue;

createApp({
  data() {
    return {
        data: undefined,
        client: {},
        accounts: [],
        userLoans: [],
        loans: [],
        id:"",
        amount:"",
        account_destinated:"",
        payments:"",
         loans_id:"",
         iva: 0
    };
  },
  created () {
    this.loadData()
    this.loan()
    // console.log(this.getIva())
  },
  methods: {
    loadData() {
        axios.get("http://localhost:8080/api/clients/current")
        .then((response) => {
            this.data = response;
         
            this.client = response.data;
            this.accounts = response.data.accounts;
            this.userLoans = response.data.loans;
       })  
        .catch(err => console.log(err))
    },
    loan() {
        axios.get("http://localhost:8080/api/loans")
        .then((response) => {
            this.loans = response.data
            console.log(this.loans)
            this.iva= this.loans.find(a => a.id == this.loans_id).iva
            console.log(this.iva)
        })
    }, 
    filterLoans:function(){
     this.loans.find(loan=> loan.id == this.loans_id)
      return this.loans.find(loan=> loan.id == this.loans_id).payments
  },
  alertLoan: function(){
    Swal.fire({
      title: 'Please confirm the loan',
      showDenyButton: true,
      confirmButtonText: 'Yes',
      denyButtonText: `Cancel`,
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire('Loan success')
       this.applyLoan()
      } else if (result.isDenied) {
        Swal.fire('Changes are not saved', '', 'info')
      }
    })
  },
    applyLoan() {
      axios.post('/api/loans', {amount:this.amount, payments:this.payments, loans_id:this.loans_id, account_destinated:this.account_destinated})
      .then(response => {
        setTimeout(()=>{
          window.location.href = '/web/accounts.html';
        },2000)
         
          })
      .catch(error => {
        Swal.fire(error.response.data)
        console.log(error)
          this.error = error.response.data;
      });

  }, 
  

  logOut(){
      console.log("hola")
      axios.post('/api/logout')
      .then(response =>{
        window.location.href = '/web/index.html';
      })
    },

  transacAlert(){
      Swal.fire({
          title: 'Are you sure to make this transaction?',
          text: "Once sent, you could recover the amount",
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#276221',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Yes,I confirm!'
        }).then((result) => {
          if (result.isConfirmed) {
            this.newTransaction()
          }
        })
      
      },

      logoutAlert(){
        Swal.fire({
            title: 'Are you sure to logout?',
            text: "",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#276221',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, confirm it!'
          }).then((result) => {
            if (result.isConfirmed) {
              this.logOut()
            }
          })
        
        },  hamb() {
          let top = document.getElementById("hTop")
          top.classList.toggle("hTopOpen")
          let mid = document.getElementById("hMid")
          mid.classList.toggle("hMidOpen")
          let bot = document.getElementById("hBot")
          bot.classList.toggle("hBotOpen")
    }
  }
}).mount("#app");
