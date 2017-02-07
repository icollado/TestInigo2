package testinigo

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EtxetikController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Etxetik.list(params), model:[etxetikCount: Etxetik.count()]
    }

    def show(Etxetik etxetik) {
        respond etxetik
    }

    def create() {
        respond new Etxetik(params)
    }

    @Transactional
    def save(Etxetik etxetik) {
        if (etxetik == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (etxetik.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond etxetik.errors, view:'create'
            return
        }

        etxetik.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'etxetik.label', default: 'Etxetik'), etxetik.id])
                redirect etxetik
            }
            '*' { respond etxetik, [status: CREATED] }
        }
    }

    def edit(Etxetik etxetik) {
        respond etxetik
    }

    @Transactional
    def update(Etxetik etxetik) {
        if (etxetik == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (etxetik.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond etxetik.errors, view:'edit'
            return
        }

        etxetik.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'etxetik.label', default: 'Etxetik'), etxetik.id])
                redirect etxetik
            }
            '*'{ respond etxetik, [status: OK] }
        }
    }

    @Transactional
    def delete(Etxetik etxetik) {

        if (etxetik == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        etxetik.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'etxetik.label', default: 'Etxetik'), etxetik.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'etxetik.label', default: 'Etxetik'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
