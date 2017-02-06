package testinigo

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class NadaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Nada.list(params), model:[nadaCount: Nada.count()]
    }

    def show(Nada nada) {
        respond nada
    }

    def create() {
        respond new Nada(params)
    }

    @Transactional
    def save(Nada nada) {
        if (nada == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (nada.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond nada.errors, view:'create'
            return
        }

        nada.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'nada.label', default: 'Nada'), nada.id])
                redirect nada
            }
            '*' { respond nada, [status: CREATED] }
        }
    }

    def edit(Nada nada) {
        respond nada
    }

    @Transactional
    def update(Nada nada) {
        if (nada == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (nada.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond nada.errors, view:'edit'
            return
        }

        nada.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'nada.label', default: 'Nada'), nada.id])
                redirect nada
            }
            '*'{ respond nada, [status: OK] }
        }
    }

    @Transactional
    def delete(Nada nada) {

        if (nada == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        nada.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'nada.label', default: 'Nada'), nada.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'nada.label', default: 'Nada'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
