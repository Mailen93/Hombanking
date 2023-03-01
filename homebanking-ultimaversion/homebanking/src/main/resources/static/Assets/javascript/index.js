const {createApp} = Vue

createApp({
    data(){
        return{
        email: '',
        password: '',
        error: '',
        firstName: '',
        lastName: '',
        cardType: '',
        cardColor: '',
    };
},
    methods: {
        login() {
            console.log(this.login)
            axios.post('/api/login', `email=${this.email}&password=${this.password}`, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })

            .then(response => {
                window.location.href = '/web/accounts.html';
            })
            .catch(error => {
                this.error = error.response.data.message;
            });
        },
        register() {
            axios.post('/api/clients', `first=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
            .then(response => {
               this.login();
            })
            .catch(error => {
                this.error = error.response.data.message;
            });
    },
    createCard(){
        axios.post('/api/clients/current/cards', `type=${this.cardType}&color=${this.cardColor}`,{
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
        .then(response => {
            window.location.href = "/web/cards.html";
        })
        .catch(error => {
            this.error = error.response.data;
            console.log(this.error)
            Swal.fire({
                icon: 'error',
                title: `${this.error}`,
                text: 'Something went wrong!',
                footer: '<a href="">Why do I have this issue?</a>'
              }) 
      });
    }
}
}).mount('#app')