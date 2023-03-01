const { createApp } = Vue

createApp({
  data() {
    return{
        data: undefined,
        client: null,
        transaction: [],
        accounts: null,

    }
  },
  created(){
    this.createTransaction()



  },
  methods:{
    createTransaction(){ let url = new URLSearchParams(location.search).get("id")
        axios.get(`http://localhost:8080/api/accounts/${url}`)
        .then (response =>{
        this.transaction = response.data.transaction
        console.log(this.transaction)
            })
            .catch(err => console.log (err))
        },
  },
  
  
  }).mount("#app")

