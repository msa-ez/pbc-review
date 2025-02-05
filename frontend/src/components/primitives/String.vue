<template>
    <div>
        <div v-if="editMode">
            <v-textarea v-if="multiLine"
                    :label="label" 
                    v-model="value"
                    @change="change"
                    outlined
                    auto-grow
                    rows="2"
            />
            <v-text-field v-else
                :label="label" 
                v-model="value"
                @change="change"
            />
        </div>
        <div v-else>
            <span v-if="showLabel">{{label}} : </span>{{value}}
        </div>
    </div>
</template>

<script>  
    export default {
        name: 'String',
        components:{
        },
        props: {
            value:{
                type: String,
                default: ''
            },
            editMode: Boolean,
            label: String,
            inputUI: String,
            showLabel: {
                type: Boolean,
                default: true
            }
        },
        computed: {
            multiLine() {
                return this.inputUI === 'TEXTAREA';
            }
        },
        methods:{
            change(){
                this.$emit("input", this.value);
            }
        }
    }
</script>
