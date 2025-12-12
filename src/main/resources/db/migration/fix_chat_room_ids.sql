-- Migration script to fix chat_rooms table
-- This updates customer_id and caregiver_id to use User IDs instead of Customer/Caregiver entity IDs

-- Update customer_id to use user_id from customers table
UPDATE chat_rooms cr
INNER JOIN bookings b ON cr.booking_id = b.id
INNER JOIN customers c ON b.customer_id = c.id
SET cr.customer_id = c.user_id
WHERE cr.customer_id != c.user_id;

-- Update caregiver_id to use user_id from caregivers table (only where caregiver is assigned)
UPDATE chat_rooms cr
INNER JOIN bookings b ON cr.booking_id = b.id
INNER JOIN caregivers cg ON b.caregiver_id = cg.id
SET cr.caregiver_id = cg.user_id
WHERE cr.caregiver_id IS NOT NULL AND cr.caregiver_id != cg.user_id;
