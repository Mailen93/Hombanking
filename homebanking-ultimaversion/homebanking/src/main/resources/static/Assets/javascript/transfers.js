const {createApp} = Vue

createApp({
    data(){
        return{
            data: [],
            accounts: [],
            transferOption:"",
            cardErrorCreation:"",
            amount:"",
            description:"",
            numberEmission:"",
            numberReceptor:"",
        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients/current")
            .then(response =>{
                this.data = response
                this.accounts = response.data.accounts
            })
        },
        transferencias(){
            axios.post('/api/transactions', `amount=${this.amount}&description=${this.description}&nDeCuentaOr=${this.numberEmission}&nDeCuentaDest=${this.numberReceptor}`,{
                headers:{
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(response => {
                Swal.fire({
                    position: 'midle',
                    icon: 'success',
                    title: 'Your login have been successful',
                    text: response.data,
                    showConfirmButton: false,
                    timer: 2000
                })
                .then(()=>{
                    swindow.location.href = '/web/transfers.html';
                })
            })
            .catch(error => {
                this.cardErrorCreation = error.response
                Swal.fire({
                    title: `${this.cardErrorCreation.data}`,
                    text: 'Please check the data and try again!',
                    icon: "error",
                    dangerMode: true,
                })
            })
        },
        parseDate(date){
            return date.split("T")[0]
        },
        soloAños(data){
            let dia = data.split("-")[1]
            let años = data.split("-")[0]
            return años.slice(2, 4)+"-"+ dia
        },
        alertLogOut(){
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, leave me out!'
            }).then((result) => {
                if (result.isConfirmed) {
                this.logout()
                }
            })
        },
        logout(){
            axios.post(`http://localhost:8080/api/logout`)
            .then(response =>{
                window.location.href = '/web/index.html';
            })
        },
    }
}).mount("#app")